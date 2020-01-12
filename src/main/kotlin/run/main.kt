package run

import files.FileUtil
import model.CustomerStatement
import parser.CustomerStatementParser
import report.ReportGenerator
import validation.CustomerStatementBatchValidator
import validation.Validation
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.asSequence


fun main() {
    // Create needed folder if they do not exist
    val inputFolder = Files.createDirectories(Path.of("./customer_statement_proccessor/input_files"))
    val successFolder = Files.createDirectories(Path.of("./customer_statement_proccessor/success"))
    val errorFolder = Files.createDirectories(Path.of("./customer_statement_proccessor/error"))
    val reportsFolder = Files.createDirectories(Path.of("./customer_statement_proccessor/reports"))
    val unsupportedFilesFolder = Files.createDirectories(Path.of("./customer_statement_proccessor/unsupported_files"))

    /**
     * Create a list of customer statements using the given file.
     */
    fun createBatchFromFile(path: Path): FileTracker<List<CustomerStatement>>? {
        println("--------------------------")
        println("Creating batch form file: ${path.fileName}")
        val customerStatementParser = CustomerStatementParser.getFor(path)

        // If no parser is found, the file type is not supported.
        if (customerStatementParser == null) {
            println("File type not supported: ${path.fileName}")
            FileUtil.moveWithTimeBasedFilename(path, unsupportedFilesFolder)
            return null
        }

        return FileTracker(path, customerStatementParser.parse(path))
    }

    fun validate(fileTracker: FileTracker<List<CustomerStatement>>): FileTracker<List<Validation<CustomerStatement>>> {
        val batch = fileTracker.value
        val validator = CustomerStatementBatchValidator(batch)
        return FileTracker(fileTracker.path, validator.validate())
    }


    fun generateReport(fileTracker: FileTracker<List<Validation<CustomerStatement>>>) {
        val statementValidation = fileTracker.value;

        // Create the report
        val document = ReportGenerator.generateFor(fileTracker.value)
        FileUtil.createFileWithTimeBasedFilename(fileTracker.path.fileName.toString()+"_report.html", reportsFolder, document)

        // Move the processed file to the succes or error folder.
        if (statementValidation.all { it.isValid() }) {
            println("All customer statements are valid for this batch")
            FileUtil.moveWithTimeBasedFilename(fileTracker.path, successFolder)
        } else {
            println("There are invalid customer statements in this batch")
            FileUtil.moveWithTimeBasedFilename(fileTracker.path, errorFolder)
        }
    }



    while (true) {
        Files.list(inputFolder)
            .asSequence()
            .filter { listedFile -> Files.isRegularFile(listedFile) }
            .map { listedFile -> createBatchFromFile(listedFile) }
            .filterNotNull()
            .map { statementBatch -> validate(statementBatch) }
            .forEach { statementValidation -> generateReport(statementValidation) }
        Thread.sleep(1000)
    }

}

/**
 * Helper class to keep knowledge of the concerning file while performing the processes
 */
class FileTracker<T>(val path: Path, val value: T)