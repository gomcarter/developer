package it.gomcarter.frameworks.base.exception;

@SuppressWarnings("serial")
public class OptimisticLockException extends CustomException {

	public OptimisticLockException() {
		this("网络连接超时，请重试");
	}

	public OptimisticLockException(Throwable e) {
		super(e);
	}

	public OptimisticLockException(String msg) {
		super(msg);
	}

}
