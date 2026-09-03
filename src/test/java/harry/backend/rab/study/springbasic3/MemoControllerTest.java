package harry.backend.rab.study.springbasic3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 웹 계층만 띄우는 슬라이스 테스트. DB나 전체 컨텍스트 없이 요청→응답 계약만 검증한다.
@WebMvcTest(controllers = MemoController.class)
@Import({MemoService.class, MemoExceptionHandler.class})
class MemoControllerTest {

    private static final String BASE = "/study/spring-basic3/memos";

    @Autowired
    MockMvc mockMvc;

    // MemoService 빈은 테스트 간에 공유되어 id가 누적되므로, 생성 응답에서 id를 읽어 쓴다.
    private int createMemo(String content) throws Exception {
        String body = mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"" + content + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    @Test
    void create_returns201WithLocation() throws Exception {
        var result = mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Spring 공부\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.content").value("Spring 공부"))
                .andReturn();

        // Location 헤더는 생성된 리소스의 실제 위치를 가리켜야 한다.
        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        mockMvc.perform(get(result.getResponse().getHeader("Location")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void update_and_delete_usePostOnlyStyle() throws Exception {
        int id = createMemo("a");

        mockMvc.perform(post(BASE + "/" + id + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"b\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("b"));

        mockMvc.perform(post(BASE + "/" + id + "/delete"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        mockMvc.perform(get(BASE + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void notFound_returnsProblemDetail() throws Exception {
        mockMvc.perform(get(BASE + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Memo Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance").value(BASE + "/999"))
                .andExpect(jsonPath("$.memoId").value(999));
    }

    @Test
    void blankContent_returns400WithFieldErrors() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.content").exists());
    }

    @Test
    void malformedJson_returns400() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Malformed Request Body"));
    }

    @Test
    void missingContentType_returns415() throws Exception {
        mockMvc.perform(post(BASE).content("{\"content\":\"x\"}"))
                .andExpect(status().isUnsupportedMediaType());
    }
}
