package it.gomcarter.frameworks.dubbo.filter;

import it.gomcarter.frameworks.base.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;

/**
 * api代理类，拦截请求，些调用日志
 *
 * @author gomcarter on 2019-11-18 14:32:39
 */
@Activate(group = CONSUMER)
@Slf4j
public class LogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
        try {
            long start = System.currentTimeMillis();
            result = invoker.invoke(invocation);
            long end = System.currentTimeMillis();

            if (result.getException() != null) {
                throw new RuntimeException(result.getException());
            }

            log.info("调用了{}接口{}.{}，耗时：{}ms, 参数：{}， 结果：{}",
                    RpcContext.getContext().getLocalAddressString(),
                    invoker.getUrl().getAbsolutePath(), invocation.getMethodName(), end - start,
                    JsonMapper.buildNonNullMapper().toJson(invocation.getArguments()),
                    JsonMapper.buildNonNullMapper().toJson(result.getValue()));

        } catch (Exception ex) {
            log.info("调用了{}接口{}.{}，报错了，参数：{}",
                    RpcContext.getContext().getLocalAddressString(),
                    invoker.getUrl().getAbsolutePath(), invocation.getMethodName(),
                    JsonMapper.buildNonNullMapper().toJson(invocation.getArguments())
                    , ex);
            throw ex;
        }
        return result;
    }
}
