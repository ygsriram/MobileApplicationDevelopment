<?php
 require_once ("/usr/share/php/Mail.php");
 require_once "/usr/share/php/Mail/mime.php";
function sendmailfac($name,$password,$mailid) {
 $from = "noreplyappraisal@gmail.com ";
 $to = $mailid;
 $subject = "Appraisal: Password for Login\r\n\r\n";
 $text = "This is a text test email message";
 $html = "
<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>

<html xmlns='http://www.w3.org/1999/xhtml'>
<head>
	<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
	<meta name='viewport' content='width=device-width, initial-scale=1.0'/>
	<title>[SUBJECT]</title>
	<style type='text/css'>

@media screen and (max-width: 600px) {
    table[class='container'] {
        width: 95% !important;
    }
}

	#outlook a {padding:0;}
		body{width:100% !important; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; margin:0; padding:0;}
		.ExternalClass {width:100%;}
		.ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {line-height: 100%;}
		#backgroundTable {margin:0; padding:0; width:100% !important; line-height: 100% !important;}
		img {outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;}
		a img {border:none;}
		.image_fix {display:block;}
		p {margin: 1em 0;}
		h1, h2, h3, h4, h5, h6 {color: black !important;}

		h1 a, h2 a, h3 a, h4 a, h5 a, h6 a {color: blue !important;}

		h1 a:active, h2 a:active,  h3 a:active, h4 a:active, h5 a:active, h6 a:active {
			color: red !important; 
		 }

		h1 a:visited, h2 a:visited,  h3 a:visited, h4 a:visited, h5 a:visited, h6 a:visited {
			color: purple !important; 
		}

		table td {border-collapse: collapse;}

		table { border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt; }

		a {color: #000;}

		@media only screen and (max-device-width: 480px) {

			a[href^='tel'], a[href^='sms'] {
						text-decoration: none;
						color: black; /* or whatever your want */
						pointer-events: none;
						cursor: default;
					}

			.mobile_link a[href^='tel'], .mobile_link a[href^='sms'] {
						text-decoration: default;
						color: orange !important; /* or whatever your want */
						pointer-events: auto;
						cursor: default;
					}
		}


		@media only screen and (min-device-width: 768px) and (max-device-width: 1024px) {
			a[href^='tel'], a[href^='sms'] {
						text-decoration: none;
						color: blue; /* or whatever your want */
						pointer-events: none;
						cursor: default;
					}

			.mobile_link a[href^='tel'], .mobile_link a[href^='sms'] {
						text-decoration: default;
						color: orange !important;
						pointer-events: auto;
						cursor: default;
					}
		}

		@media only screen and (-webkit-min-device-pixel-ratio: 2) {
			/* Put your iPhone 4g styles in here */
		}

		@media only screen and (-webkit-device-pixel-ratio:.75){
			/* Put CSS for low density (ldpi) Android layouts in here */
		}
		@media only screen and (-webkit-device-pixel-ratio:1){
			/* Put CSS for medium density (mdpi) Android layouts in here */
		}
		@media only screen and (-webkit-device-pixel-ratio:1.5){
			/* Put CSS for high density (hdpi) Android layouts in here */
		}
		/* end Android targeting */
		h2{
			color:#181818;
			font-family:Helvetica, Arial, sans-serif;
			font-size:22px;
			line-height: 22px;
			font-weight: normal;
		}
		a.link1{

		}
		a.link2{
			color:#fff;
			text-decoration:none;
			font-family:Helvetica, Arial, sans-serif;
			font-size:16px;
			color:#fff;border-radius:4px;
		}
		p{
			color:#555;
			font-family:Helvetica, Arial, sans-serif;
			font-size:16px;
			line-height:160%;
		}
	</style>

<script type='colorScheme' class='swatch active'>
  {
    'name':'Default',
    'bgBody':'ffffff',
    'link':'fff',
    'color':'555555',
    'bgItem':'ffffff',
    'title':'181818'
  }
</script>

</head>
<body>
	
	<table cellpadding='0' width='100%' cellspacing='0' border='0' id='backgroundTable' class='bgBody'>
	<tr>
		<td>
	<table cellpadding='0' width='620' class='container' align='center' cellspacing='0' border='0'>
	<tr>
		<td>
		
		

		<table cellpadding='0' cellspacing='0' border='0' align='center' width='600' class='container'>
			<tr>
				<td class='movableContentContainer bgItem'>

					<div class='movableContent'>
						<table cellpadding='0' cellspacing='0' border='0' align='center' width='600' class='container'>
							<tr height='40'>
								<td width='200'>&nbsp;</td>
								<td width='200'>&nbsp;</td>
								<td width='200'>&nbsp;</td>
							</tr>
							<tr>
								<td width='200' valign='top'>&nbsp;</td>
								<td width='200' valign='top' align='center'>
									<div class='contentEditableContainer contentImageEditable'>
					                	<div class='contentEditable' align='center' >
					                  		<img src='http://i.imgur.com/J32wBSk.png' width='100%' height='100%'  alt='Logo'  data-default='placeholder' />


					                	</div>
					              	</div>
								</td>
								<td width='200' valign='top'>&nbsp;</td>
							</tr>
							<tr height='0'>
								
								
							</tr>
						</table>
					</div>

					<div class='movableContent'>
						<table cellpadding='0' cellspacing='0' border='0' align='center' width='1000' class='container'>
							<tr>
								<td width='100'>&nbsp;</td>
								<td width='400' align='center'>
									<div class='contentEditableContainer contentTextEditable'>
					                	<div class='contentEditable' align='left' >
					                  		<p >Hi Prof. $name,
					                  			<br/>
					                  			<br/>
												Welcome to the Appraisal application. This is an application that helps you to get in touch with student, with continous feedback system which will help to appraise the effectiveness of the topic delevired. The goal is to help the student gain knowledge by understanding the topic better.<br/><br/>Your Login creditinals are '$password'.<br/><br/>In case of any issues get back to us at nandan.beis.12@acharya.ac.in or contact admin.<br/><br/>Thanks<br/><img src='http://i.imgur.com/WySAVOI.png' width='100' height='25'  alt='Logo'  data-default='placeholder' /></p>
					                	</div>
					              	</div>
								</td>
								<td width='100'>&nbsp;</td>
							</tr>
						</table>
						
					</div>


					<div class='movableContent'>
						<table cellpadding='0' cellspacing='0' border='0' align='center' width='600' class='container'>
							<tr>
								<td width='100%' colspan='2' style='padding-top:10px;'>
									<hr style='height:1px;border:none;color:#333;background-color:#ddd;' />
								</td>
							</tr>
				
	
												</table>
	

</body>
</html>
";
 $crlf = "\n";
 $mime = new Mail_mime($crlf); 
 $mime->setTXTBody($text); 
 $mime->setHTMLBody($html);
 $host = "ssl://smtp.gmail.com";
 $username = "noreplyappraisal@gmail.com";
 $password = "appraisal@!@#";
 
 $headers = array ('From' => $from,
  'To' => $to,
  'Subject' => $subject);
 $smtp = Mail::factory('smtp',
  array ('host' => $host,
	'port'=>'465',
    'auth' => true,
    'username' => $username,
    'password' => $password));

 
 $body = $mime->get();
 $headers = $mime->headers($headers); 
 
 $mail = $smtp->send($to, $headers, $body);
 
 if (PEAR::isError($mail)) {
  echo("

" . $mail->getMessage() . "
");
} else {
  
}
}
?>
