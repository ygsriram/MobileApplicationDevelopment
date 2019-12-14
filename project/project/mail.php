<?php
$name = "Nandan";
$email = "anandan428@gmail.com";
$message = "hello world";
$from = 'From: anandan428@gmail.com'; 
$to = 'nandan.beis.12@acharya.ac.in'; 
$subject = 'Customer Inquiry';
$body = "From: $name\n E-Mail: $email\n Message:\n $message";

$headers .= "MIME-Version: 1.0\r\n";
$headers .= "Content-type: text/html\r\n";
$headers = 'From: anandan428@gmail.com' . "\r\n" .
'Reply-To: reply@example.com' . "\r\n" .
'X-Mailer: PHP/' . phpversion();
if(mail($to, $subject, $message, $headers))
{
echo "Mail sent Sucessfully";
}
else
{
echo "Unsucessfull";
}
?> 
