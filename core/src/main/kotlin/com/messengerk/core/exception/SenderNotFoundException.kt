package com.messengerk.core.exception

class SenderNotFoundException(name: String) : Exception("Sender with name $name was not found"), MessageBusException