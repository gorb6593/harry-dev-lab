package harry.backend.rab.day2_260604.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import harry.backend.rab.day2_260604.service.BeanLearningService;
import harry.backend.rab.day2_260604.service.BeanReportResponse;
import harry.backend.rab.day2_260604.service.LifecycleReportResponse;
import harry.backend.rab.day2_260604.service.PriceCalculationResponse;
import harry.backend.rab.day2_260604.service.TransactionProbeResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/day2/beans")
@RequiredArgsConstructor
public class BeanLearningController {

	private final BeanLearningService beanLearningService;

	@GetMapping("/report")
	public BeanReportResponse report() {
		return beanLearningService.report();
	}

	@GetMapping("/price")
	public PriceCalculationResponse calculatePrice(
		@RequestParam(defaultValue = "10000") int originalPrice
	) {
		return beanLearningService.calculatePrice(originalPrice);
	}

	@GetMapping("/lifecycle")
	public LifecycleReportResponse lifecycle() {
		return beanLearningService.lifecycleReport();
	}

	@GetMapping("/transaction-proxy")
	public TransactionProbeResponse transactionProxy() {
		return beanLearningService.transactionProxyReport();
	}
}
