/**
 * Some logging technics
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.1
 * @date 2018-01-27
 * @see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-002-log
 */

import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg

import com.sap.it.api.ITApiFactory
import com.sap.it.spi.ITApiHandler
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.UserCredential
import com.sap.it.api.securestore.exception.SecureStoreException
import com.sap.it.api.keystore.KeystoreService
import com.sap.it.api.keystore.exception.KeystoreException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

CpiMsg mplDemo(CpiMsg msg) {
	def mpl = messageLogFactory.getMessageLog(msg)
	mpl.addAttachmentAsString("attachment1", "1234567890\n"*30+"."*30, "text/plain")
	mpl.addAttachmentAsString("attachment2", "<a><b c='1'><![CDATA[ text here ]]><!-- comment --></b></a>", "application/xml")
	throw new javax.script.ScriptException("Modelled exception thrown by us, at function mplDemo(msg)")
	msg
}

CpiMsg mplCatch(CpiMsg msg) {
	def mpl = messageLogFactory.getMessageLog(msg)
	Exception e = msg.properties.CamelExceptionCaught
	String xlog = "$e"
	mpl.addAttachmentAsString("exception", xlog, "text/plain")
	msg.setBody(xlog)
	msg
}

CpiMsg customModel(CpiMsg msg) {
	com.sap.it.api.securestore.UserCredential cred = ITApiFactory.getApi(SecureStoreService.class, null).getUserCredential(msg.properties.Credential_Sftp)

	Object log002 = msg.properties.log002
	log002.connectSftp(msg.properties.Host_Sftp, cred, false)

	log002.mkdirCdSftp('/outgoing/cpi/custom-test')
	log002.putSftp("azaza.txt", "1\n2\n3\n"*100, true)

	Path x = log002.createTempFile("1_.txt")
    OutputStream w = Files.newOutputStream(x)
    String b = " " * 11
    (1..1).each{
        w.write(b.getBytes())
    }
    w.close()
//    log002.tempToArchive("azaza_1234567.zip")
//	log002.disconnect()
	msg
}

CpiMsg customCatch(CpiMsg msg) {
	msg
}