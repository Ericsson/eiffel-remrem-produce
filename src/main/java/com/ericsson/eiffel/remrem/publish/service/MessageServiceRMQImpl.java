package com.ericsson.eiffel.remrem.publish.service;

import com.ericsson.eiffel.remrem.publish.helper.RMQHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

@Service("messageServiceRMQImpl") @Slf4j public class MessageServiceRMQImpl
    implements MessageService {

    private static final String SUCCEED = "succeed";
    @Autowired @Qualifier("rmqHelper") RMQHelper rmqHelper;

    @Override public ListenableFuture<List<SendResult>> send(String routingKey, List<String> msgs) {
        List<SendResult> results = new ArrayList<>();
        if (!CollectionUtils.isEmpty(msgs)) {
            for (String msg : msgs) {
                results.add(send(routingKey, msg));
            }
        }
        return new AsyncResult<List<SendResult>>(results);
    }

    private SendResult send(String routingKey, String msg) {
        String resultMsg = SUCCEED;
        try {
            rmqHelper.send(routingKey, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultMsg = e.getStackTrace().toString();
        }
        return new SendResult(resultMsg);
    }
}
