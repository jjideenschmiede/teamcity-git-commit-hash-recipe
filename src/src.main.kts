@file:Repository("https://download.jetbrains.com/teamcity-repository/")
@file:DependsOn("org.jetbrains.teamcity:serviceMessages:2024.12")

import jetbrains.buildServer.messages.serviceMessages.ServiceMessage
import jetbrains.buildServer.messages.serviceMessages.ServiceMessageTypes.BUILD_SET_PARAMETER

val variableName: String = System.getenv("input_variable_name") ?: error("Input \"variable_name\" is not set.")
val hashLength: Int = System.getenv("input_hash_length")?.toInt() ?: error("Input \"hash_length\" is not set.")

val fullCommitHash: String = System.getenv("BUILD_VCS_NUMBER")
	?: error("VCS revision not available. Make sure this build is triggered by a VCS change.")

val shortCommitHash: String = fullCommitHash.take(hashLength)
val message = ServiceMessage.asString(
	BUILD_SET_PARAMETER,
	mapOf(
		"name" to "env.$variableName",
		"value" to shortCommitHash
	)
)
println(message)
println("Environment variable \"$variableName\" set to: $shortCommitHash")
