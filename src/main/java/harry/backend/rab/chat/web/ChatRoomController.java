package harry.backend.rab.chat.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harry.backend.rab.chat.dto.CreateRoomRequest;
import harry.backend.rab.chat.dto.JoinRoomRequest;
import harry.backend.rab.chat.dto.RoomResponse;
import harry.backend.rab.chat.dto.UserResponse;
import harry.backend.rab.chat.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService roomService;

	@PostMapping
	public ResponseEntity<RoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
		RoomResponse created = roomService.create(request.name());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public List<RoomResponse> findAll() {
		return roomService.findAll();
	}

	@PostMapping("/{roomId}/members")
	public ResponseEntity<Void> join(@PathVariable Long roomId, @Valid @RequestBody JoinRoomRequest request) {
		roomService.join(roomId, request.userId());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{roomId}/members")
	public List<UserResponse> members(@PathVariable Long roomId) {
		return roomService.findMembers(roomId);
	}
}
