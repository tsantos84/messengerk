package com.messengerk.core.exception

class MessageBusNotFoundException(name: String) : RuntimeException("Message bus with name \"$name\" was not found")