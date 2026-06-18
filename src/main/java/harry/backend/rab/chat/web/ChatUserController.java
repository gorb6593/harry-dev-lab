package harry.backend.rab.chat.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harry.backend.rab.chat.dto.CreateUserRequest;
import harry.backend.rab.chat.dto.UserResponse;
import harry.backend.rab.chat.service.ChatUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat/users")
@RequiredArgsConstructor
public class ChatUserController {

	private final ChatUserService userService;

	@PostMapping
	public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
		UserResponse created = userService.create(request.username(), request.displayName());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public List<UserResponse> findAll() {
		return userService.findAll();
	}
}
