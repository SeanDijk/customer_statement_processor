package report

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.stream.appendHTML
import kotlinx.html.stream.createHTML
import model.CustomerStatement
import org.w3c.dom.Document
import validation.Validation

object ReportGenerator {

    fun generateFor(validated: List<Validation<CustomerStatement>>): String {
        val invalid = validated.any { validation -> validation.isNotValid() }
        return createHTML(prettyPrint = true, xhtmlCompatible = false)
            .div {
                    h1 { +"Customer statement report" }
                    if (invalid) {
                        generateInvalidSection(validated)
                    } else {
                        p { +"All records are valid." }
                    }
                }
    }

    private fun DIV.generateInvalidSection(validated: List<Validation<CustomerStatement>>) {
        h2 { +"Invalid: " }
        table {
            thead {
                tr {
                    th { +"Reference" }
                    th { +"Description" }
                    th { +"Reason" }
                }
            }
            tbody {
                validated.filter { it.isNotValid() }.forEach {
                    tr {
                        td { +it.validated.reference.toString() }
                        td { +it.validated.description }
                        td {
                            it.invalidRules.forEachIndexed() { index, s ->
                                if (index > 0) {
                                    br { }
                                }
                                +s
                            }
                        }
                    }
                }
            }
        }
    }


}