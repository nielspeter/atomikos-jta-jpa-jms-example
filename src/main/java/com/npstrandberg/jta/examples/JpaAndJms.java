package com.npstrandberg.jta.examples;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaAndJms {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private PersonRepository personRepository;

    @Transactional(rollbackFor = Exception.class)
    public void registerNewPerson(Person person, boolean fail) {
        personRepository.save(person);
        jmsTemplate.convertAndSend("PERSON_CREATED", "{name: '" + person.getName() + "'}");
        if (fail) throw new IllegalStateException("BOOM");
    }

    @Transactional
    public String receivePersonCreatedMessage() {
        return (String) jmsTemplate.receiveAndConvert("PERSON_CREATED");
    }
}

