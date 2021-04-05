package org.sun.bright.manager.mq.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * JMS Producer
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 * @version 1.0
 * @since 2021/03/01
 */
@Component
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsOperations jmsOperations;


}
