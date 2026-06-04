package harry.backend.rab.day2_260604.service;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import harry.backend.rab.day2_260604.domain.Day2Product;
import harry.backend.rab.day2_260604.repository.Day2ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeanLearningService {

	private final ApplicationContext applicationContext;
	private final Day2ProductRepository productRepository;
	private final DiscountPolicy discountPolicy;
	private final TransactionProbeService transactionProbeService;
	private final LifecycleTraceBean lifecycleTraceBean;
	private final TransactionSelfInvocationService transactionSelfInvocationService;

	public BeanReportResponse report() {
		return new BeanReportResponse(
			beanExists("beanLearningController"),
			beanExists("beanLearningService"),
			beanExists("day2MemoryProductRepository"),
			beanExists("fixedDiscountPolicy"),
			productRepository.getClass().getName(),
			discountPolicy.getClass().getName(),
			transactionProbeService.getClass().getName(),
			AopUtils.isAopProxy(transactionProbeService),
			AopUtils.getTargetClass(transactionProbeService).getName(),
			transactionProbeService.currentTransactionMode()
		);
	}

	public PriceCalculationResponse calculatePrice(int originalPrice) {
		Day2Product product = productRepository.findById(1L).orElseThrow();
		int discountedPrice = discountPolicy.discount(originalPrice);

		return new PriceCalculationResponse(
			product.name(),
			originalPrice,
			discountedPrice,
			discountPolicy.getClass().getSimpleName()
		);
	}

	public LifecycleReportResponse lifecycleReport() {
		return new LifecycleReportResponse(
			lifecycleTraceBean.getClass().getName(),
			AopUtils.isAopProxy(lifecycleTraceBean),
			AopUtils.getTargetClass(lifecycleTraceBean).getName(),
			lifecycleTraceBean.events()
		);
	}

	public TransactionProbeResponse transactionProxyReport() {
		TransactionSelfInvocationService.TransactionState externalCallState =
			transactionSelfInvocationService.transactionalReadOnlyState();
		TransactionProbeResponse selfInvocationReport =
			transactionSelfInvocationService.compareExternalAndInternalCall();

		return new TransactionProbeResponse(
			externalCallState.active(),
			externalCallState.readOnly(),
			selfInvocationReport.selfInvocationTransactionActive(),
			selfInvocationReport.selfInvocationReadOnly()
		);
	}

	private boolean beanExists(String beanName) {
		return applicationContext.containsBean(beanName);
	}
}
