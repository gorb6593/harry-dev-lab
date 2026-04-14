package harry.backend.rab.jpaLevel7.controller;

import harry.backend.rab.jpaLevel7.dto.ConcurrencyObservationResponse;
import harry.backend.rab.jpaLevel7.dto.StockCreateRequest;
import harry.backend.rab.jpaLevel7.dto.StockDecreaseRequest;
import harry.backend.rab.jpaLevel7.dto.StockDecreaseWithDelayRequest;
import harry.backend.rab.jpaLevel7.dto.StockResponse;
import harry.backend.rab.jpaLevel7.service.StockLevel7Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jpaLevel7/stocks")
public class StockLevel7Controller {

    private final StockLevel7Service stockLevel7Service;

    @PostMapping
    public ResponseEntity<StockResponse> create(@RequestBody StockCreateRequest request) {
        StockResponse response = stockLevel7Service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{stockItemId}")
    public StockResponse findById(@PathVariable Long stockItemId) {
        return stockLevel7Service.findById(stockItemId);
    }

    @PatchMapping("/{stockItemId}/decrease")
    public ConcurrencyObservationResponse decrease(
            @PathVariable Long stockItemId,
            @RequestBody StockDecreaseRequest request
    ) {
        return stockLevel7Service.decrease(stockItemId, request);
    }

    @PatchMapping("/{stockItemId}/decreaseWithDelay")
    public ConcurrencyObservationResponse decreaseWithDelay(
            @PathVariable Long stockItemId,
            @RequestBody StockDecreaseWithDelayRequest request
    ) {
        return stockLevel7Service.decreaseWithDelay(stockItemId, request);
    }
}
