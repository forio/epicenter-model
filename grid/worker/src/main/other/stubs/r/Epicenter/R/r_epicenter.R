#' @title Subscribe a listener to an event
#' @description
#' Subscribes a function to an event for later callback
#' @param event character, an event name
#' @param listener function, the callback fnction
#' @keywords epicenter
#' @export
subscribe <- function(event, listener) {
}

#' @title Publish an event, calling it's callback function
#' @description
#' Calls the callback function, if any, registered to an event, with the provided arguments
#' @param event character, an event name
#' @param args arguments, the arguments to the callback function
#' @keywords epicenter
#' @export
publish <- function(event, args = list()) {
}

#' @title Record the value of a variable
#' @description
#' Store the value of a variable by it's name for external access
#' @param name character, an variable name
#' @param value object, the value to be converted to a json string and stored
#' @keywords epicenter
#' @export
record <- function(name, value) {
}

#' @title Log a message to the worker log
#' @description
#' Log a message to the worker log
#' @param level character, one of ('TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR' or 'FATAL')
#' @param message character, the message to be logged
#' @keywords epicenter
#' @export
log <- function(level, message) {
}

#' @title Call an external function
#' @description
#' Call an external function
#' @param name character, the name of the function
#' @param arguments character, [] of arguments to the function as a Json[]
#' @keywords epicenter
#' @export
callback <- function(name, arguments) {
}

#' @title Register a custom getter for variable get commands and procedure calls
#' @description
#' Register a custom json serializer for a non-standard class or private encoding.
#' @param custom_class character, class of the custom variable
#' @param custom_encoder function, the variable retrieval function which must accept...
#'        name character, the field name of the object being retrieved, whihc might be
#'        NULL in the case of arbitrary functions (procedure calls)
#         variable object, the current value of the object
#         variable_class character, the class of the object
#' @keywords epicenter
#' @export
register_custom_encoder <- function(custom_class, custom_encoder) {
}

#' @title Register a custom updater for variable set commands
#' @description
#' Register a custom json deserializer for a non-standard class or private encoding.
#' @param custom_class character, class of the custom variable
#' @param custom_decoder function, the variable updating function which must accept...
#'        name character, the field name of the object being updated
#         variable object, the current value of the object
#         variable_class character, the class of the object
#         value character, the updated value as a string
#' @keywords epicenter
#' @export
register_custom_decoder <- function(custom_class, custom_decoder) {
}
