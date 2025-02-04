package net.twisterrob.gradle.quality

import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

class Violations(
	@JvmField val parser: String,
	@JvmField val module: String,
	@JvmField val variant: String,
	/**
	 * Parseable result.
	 */
	@JvmField val result: File,
	/**
	 * Human-consumable report.
	 */
	@JvmField val report: File,
	/**
	 * Report file missing, or error during read.
	 */
	@JvmField val violations: List<Violation>?
) {

	override fun toString(): String {
		val violations = this.violations?.joinToString(prefix = "\n", separator = "\n")
		return "${module}${if (module == ":") "" else ":"}${parser}@${variant} (${result}):$violations"
	}
}

class Violation(
	val rule: String,
	val category: String?,
	val severity: Severity,
	val message: String,
	val specifics: Map<String, String> = emptyMap(),
	val location: Location,
	val source: Source
) {

	override fun toString(): String =
		"Violation(rule='$rule', category=$category, severity=$severity, message='$message', specifics=$specifics, location=$location, source=$source)"

	enum class Severity {
		INFO,
		WARNING,
		ERROR
	}

	class Location(
		val module: Project,
		val task: Task,
		val variant: String,
		val file: File,
		val startLine: Int,
		val endLine: Int,
		val column: Int
	) {
		override fun toString(): String =
			"Location(module=$module, task=$task, variant='$variant', file=$file, startLine=$startLine, endLine=$endLine, column=$column)"
	}

	class Source(
		val parser: String,
		val gatherer: String,
		val reporter: String,
		val source: String,
		val report: File,
		val humanReport: File?
	) {
		override fun toString(): String =
			"Source(parser='$parser', gatherer='$gatherer', reporter='$reporter', source='$source', report=$report, humanReport=$humanReport)"
	}
}
