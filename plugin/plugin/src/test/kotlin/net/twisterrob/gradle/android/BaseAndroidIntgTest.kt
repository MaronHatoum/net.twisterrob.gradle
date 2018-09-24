package net.twisterrob.gradle.android

import net.twisterrob.gradle.BaseIntgTest
import org.intellij.lang.annotations.Language
import org.junit.Rule
import org.junit.rules.TestName
import kotlin.test.BeforeTest

abstract class BaseAndroidIntgTest : BaseIntgTest() {
	@Suppress("PropertyName")
	@Rule @JvmField val __testName = TestName()

	@BeforeTest fun setUp() {
		@Language("xml")
		val androidManifest = """
			<manifest package="${packageName}" />
		""".trimIndent()
		gradle.file(androidManifest, "src/main/AndroidManifest.xml")

		@Language("properties")
		val gradleProperties = """
			# suppress inspection "UnusedProperty"
			android.enableAapt2=false
		""".trimIndent()
		gradle.file(gradleProperties, "gradle.properties")

		if (__testName.methodName.endsWith(" (release)")) {
			createFileToMakeSureProguardPasses()
		}
	}

	protected fun createFileToMakeSureProguardPasses() {
		@Language("java")
		val apkContentForProguard = """
			package ${packageName};
			class AClassToSatisfyProguard {
				@android.webkit.JavascriptInterface public void f() {}
			}
		""".trimIndent()
		gradle.file(apkContentForProguard, "src/main/java/${packageFolder}/AClassToSatisfyProguard.java")
	}
}
