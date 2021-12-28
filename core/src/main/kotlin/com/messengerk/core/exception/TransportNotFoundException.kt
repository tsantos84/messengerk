package com.messengerk.core.exception

class TransportNotFoundException(name: String) : Exception("Sender with name $name was not found"), MessageBusException