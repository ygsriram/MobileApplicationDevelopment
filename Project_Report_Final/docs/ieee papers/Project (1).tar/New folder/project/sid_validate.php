<?php
require_once 'include/DB_Functions.php';
include 'pws_func.php';
include 'mailtester.php';
include 'mailtesterfac.php';
$i=0;
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (!empty($_POST['sid']) ) {
    // receiving the post params
    $sid = $_POST['sid'];
    $deviceid = $_POST['deviceid'];
$ram=$db->isUserExisted($sid); 
$type1 = $db->type_user($sid);
    if ($ram) {
        // Id is found
	$user=$db->login_status($sid);
	if(!$user)
	{
	try {
	$password = random_str(6);
	} catch(Exception $e) {
	echo $e;
	}
	if(strcmp($type1["type"],"student")==0){
	$mailid=$db->getstudentmailid($sid);
	$mail_id=$mailid["Email_ID"];
	$name = $mailid["Name"];
$response["error"] = FALSE;
	$response["ide"]=1;
	$response["mail"]=$mail_id;
	$response["error_msg"] = "Login credentials are sent to your mail id $mail_id. Please check your spam folder. Report the problem if mail not recevied within 5 mins. Thank you"; 
	
	
	$response["pwd"] = $password;
	$db->storeUser($sid,$password);
	//sendmail($mailid["Name"],$mailid["Email_ID"],$password);
	echo json_encode($response);
	sendmail($name,$password,$mail_id);
	
}else{
	$mailid=$db->getfacultymailid($sid);
	$mail_id=$mailid["Email_ID"];
	$name = $mailid["Name"];
$response["error"] = FALSE;
	$response["ide"]=1;
	$response["mail"]=$mail_id;
	$response["error_msg"] = "Login credentials are sent to your mail id $mail_id. Please check your spam folder. Report the problem if mail not recevied within 5 mins. Thank you"; 
	
	
	$response["pwd"] = $password;
	$db->storeUser($sid,$password);
	//sendmail($mailid["Name"],$mailid["Email_ID"],$password);
	echo json_encode($response);
	sendmailfac($name,$password,$mail_id);
}      

  
	} else if($user["deviceid"] == $deviceid) {
		$response["error"] = FALSE;
		$response["deviceid"]=$deviceid;
		$response["ide"]=0;
		$user=$db->userdetails($sid);
		$response["Sid"] = $user["Sid"];
        	$response["user"]["Name"] = $user["Name"];
        	$response["user"]["Semester"] = $user["Semester"];
        	$response["user"]["Section"] = $user["Section"];
        	$response["user"]["Department"] = $user["Department"];
		$response["user"]["Email_ID"] = $user["Email_ID"];
        	$response["user"]["Mobile_number"] = $user["Mobile_number"];
		$abc=$db->sub_count($user["Semester"]);
		$j=$abc["count(*)"];
		$k=0;
		$sub = $db->sub_details($user["Semester"]);
		while($i!=$j)
		{
		$response["subject"]["entry"]=$j;
		$response["subject"]["sub$i"] = $sub[$k];
		$response["subject"]["subcode$i"] = $sub[$k+1];
		$i++;
		$k+=2;
		}
		echo json_encode($response);
}
else {
	$response["error"] = 'TRUE';
	$response["error_msg"] = "You are already logged in. Please contact the admin.";
	echo json_encode($response);
	}
    	} else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "ID Entered is wrong, Please enter it properly";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter SID is missing!";
    echo json_encode($response);
}
?>
