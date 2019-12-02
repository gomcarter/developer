package ${entity.servicePackageName};

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${entity.basePackageName}.domain.AbstractFsmInstance;
import ${entity.basePackageName}.domain.AbstractFsmLog;
import ${entity.basePackageName}.service.FsmService;
import ${entity.basePackageName}.utils.FsmUtils;
import ${entity.basePackageName}.dao.AbstractFsmInstanceDao;
import ${entity.basePackageName}.dao.AbstractFsmLogDao;

import ${entity.entityPackageName}.${entity.className};
import ${entity.entityPackageName}.${entity.fsmClassName};
import ${entity.entityPackageName}.${entity.fsmClassName}FsmLog;
import ${entity.servicePackageName}.${entity.fsmClassName}Service;

@Service
@Transactional
public class ${entity.fsmClassName}FsmService extends FsmService {
	@Autowired
	private ${entity.fsmClassName}Service ${entity.fsmInstanceClassName}Service;
	
	@Autowired
	public void setFsmLogDao(
		final @Qualifier("${entity.fsmInstanceClassName}FsmLogDaoImpl") AbstractFsmLogDao fsmLogDao) {
		super.setFsmLogDao(fsmLogDao);
	}

	@Autowired
	public void setFsmInstanceDao(
		final @Qualifier("${entity.fsmInstanceClassName}FsmInstanceDaoImpl") AbstractFsmInstanceDao fsmInstanceDao) {
		super.setFsmInstanceDao(fsmInstanceDao);
	}

	@PostConstruct
	public void init() {
		/*在FsmUtils.fsmType 中手动添加 ${entity.fsmInstanceClassName} */
		super.setType(FsmUtils.fsmType.${entity.fsmInstanceClassName}.toString());
	}
	
	@Override
	protected AbstractFsmLog createLog(Long objectId) {
		return new ${entity.fsmClassName}FsmLog();
	}

	@Override
	protected AbstractFsmInstance createInstance() {
		return new ${entity.fsmClassName}FsmInstance();
	}
	
	@Override
	public AbstractFsmInstance processEvent(String event,Long objectId,String comment) {
		AbstractFsmInstance instance = super.processEvent(event, objectId,comment);
		notifyStateChange(objectId, instance.getState(), comment);
		return instance;
	}
	
	@Override
	public AbstractFsmInstance create(Long objectId) {
		AbstractFsmInstance instance = super.create(objectId);
		notifyStateChange(objectId, instance.getState(), "");
		return instance;
	}
	
	/**
	 * 同步entity状态
	 */
	private void notifyStateChange(Long objectId, String state, String comment) {
		this.notifyStateChange(objectId, state, null, comment);
	}
	
	private void notifyStateChange(Long objectId, String state, Integer reasonId, String comment) {
${entity.fsmClassName} ${entity.fsmInstanceClassName} = ${entity.fsmInstanceClassName}Service.queryById(objectId);
		logger.info("${entity.fsmClassName}:" + objectId + "changed from :" + ${entity.fsmInstanceClassName}.getState() + ",to :" + state);
${entity.fsmInstanceClassName}.setState(state);//更新状态
	}
}
