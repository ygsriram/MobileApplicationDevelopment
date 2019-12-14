<?php
function sendmail($name,$emailid,$password)
{
$email = "anandan428@gmail.com";
$message = "Hi $name, Your Password for login is $password. Please authencitate yoursely as soon as you receive this mail.";
$from = 'From: anandan428@gmail.com'; 
$to = $emailid; 
$subject = 'Login credentials';
$body = "From: $name\n E-Mail: $email\n Message:\n $message";
$headers .= "MIME-Version: 1.0\r\n";
$headers .= "Content-type: text/html\r\n";
$headers = 'From: anandan428@gmail.com' . "\r\n" .
'X-Mailer: PHP/' . phpversion();
if(mail($to, $subject, $message, $headers))
{
echo "Mail sent Sucessfully";
}
else
{
echo "Unsucessfull";
}
}
?> 
