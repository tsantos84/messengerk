package com.messengerk.core.exception

class NoSenderRoutedToMessageException(name: Any) : Exception("No sender routed to message ${name::class.qualifiedName}"), MessageBusException