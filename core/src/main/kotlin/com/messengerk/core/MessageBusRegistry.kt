package com.messengerk.core

typealias MessageBusLocator = () -> MessageBus

class MessageBusRegistry : HashMap<String, MessageBusLocator>()