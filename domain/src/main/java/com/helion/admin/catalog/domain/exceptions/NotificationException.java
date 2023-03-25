package com.helion.admin.catalog.domain.exceptions;

import com.helion.admin.catalog.domain.validation.handler.Notification;

public class NotificationException extends DomainException{
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
