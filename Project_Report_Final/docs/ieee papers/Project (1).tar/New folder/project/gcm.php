<?php
	//generic php function to send GCM push notification
	require("config.inc.php");
//require_once 'include/DB_Functions.php';
//$db = new DB_Functions();
	
   function sendPushNotificationToGCM($registatoin_ids, $fid, $subcode,$subname,$fname) {
		//Google cloud messaging GCM-API url
        $url = 'https://android.googleapis.com/gcm/send';
        $fields = array(
            'registration_ids' => $registatoin_ids,
            'data' => array( "fid" => $fid , "subcode" => $subcode,"subname" => $subname,"fname" => $fname)
	     
        );
		// Google Cloud Messaging GCM API Key
		define("GOOGLE_API_KEY", "AIzaSyB2sUhehlrVepsxi5OlXXDe8sXQB15QEWQ"); 		
        $headers = array(
            'Authorization: key=' . GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);	
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);				
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);
        return $result;
    }
?>
<?php
	//this block is to post message to GCM on-click
	$conn = mysqli_connect("localhost", "root", "","project");

	if (!$conn) {
   	 	echo "Unable to connect to DB: " . mysqli_error();
   	 	exit;
	}
	/*$abc = mysqli_select_db($conn,"project");
	if (!$abc) {
    	echo "Unable to select mydbname: " . mysqli_error();
    exit;
}*/

	$pushStatus = "";	
	if(!empty($_POST["subcode"]&&$_POST["sec"]&&$_POST["room"]&&$_POST["fid"]) ){	
		//$gcmRegID  = file_get_contents("GCMRegId.txt");
		
		$subcode=$_POST["subcode"];
		$sec=$_POST["sec"];
		$fid=$_POST["fid"];
		$result = mysqli_query($conn,"select * from no_class_taken where subcode = '$subcode' and sec = '$sec'");
		if($result->num_rows>0){
		$query = "update no_class_taken set noct = noct+1, date = CURDATE() where subcode = '$subcode' and sec = '$sec'";
		mysqli_query($conn,$query);
}else
{

mysqli_query($conn,"INSERT INTO no_class_taken(subcode, fid, noct, sec,date) VALUES ('$subcode',NULL,1,'$sec',CURDATE())");
}

		$query = "SELECT regID FROM userid, student_details WHERE student_details.Sid = userid.Sid and student_details.Semester=(select sem from subject_details where subcode = '$subcode') and student_details.Section='$sec'";
    			if($query_run = mysqli_query($conn,$query)) {

     				   $gcmRegIds = array();
     				   while($query_row = mysqli_fetch_assoc($query_run)) {

          				  array_push($gcmRegIds, $query_row["regID"]);



       				   }

    		}
		$pushMessage = "Message to transmit via GCM";
		if (isset($gcmRegIds) && isset($pushMessage)) {		
			//$gcmRegIds = array($gcmRegID);
			$message =  $pushMessage;
			$query1 = "SELECT Name FROM `faculty_details` WHERE Fid = '$fid'";
			if($query_run1 = mysqli_query($conn,$query1))
			$fname = mysqli_fetch_assoc($query_run1);
			//echo $fname["Name"];	
			$query1 = "SELECT subject FROM `subject_details` WHERE subcode = '$subcode'";
			if($query_run1 = mysqli_query($conn,$query1))
			$subname = mysqli_fetch_assoc($query_run1);
			//echo $subname["subject"];
			$pushStatus = sendPushNotificationToGCM($gcmRegIds, $fid,$subcode,$subname["subject"],$fname["Name"]);
			$response["error"]=FALSE;
			$response["err_msg"]="Appraisal Request sent";
			echo json_encode($response);
		}		
	}
	
	//this block is to receive the GCM regId from external (mobile apps)
	if(!empty($_POST["regID"]&&$_POST["Sid"])) {
		$gcmRegID  = $_POST["regID"]; 
		$Sid12=$_POST["Sid"];
		//file_put_contents("GCMRegId.txt",$gcmRegID);
		$result=mysqli_query($conn,"select * from userid where Sid = '$Sid12'");
		if($result->num_rows==0){
		$query = "INSERT INTO userid ( regID, Sid ) VALUES ( :regID1, :Sid1 ) ";
    
   		 //Again, we need to update our tokens with the actual data:
   		 $query_params = array(
      	         ':regID1' => $gcmRegID,
   	         ':Sid1' => $Sid12
   		 );
  		try {
     		   	  $stmt   = $db->prepare($query);
      			  $result = $stmt->execute($query_params);
   		 }
   		 catch (PDOException $ex) {
    		    // For testing, you could use a die and message. 
     		    //die("Failed to run query: " . $ex->getMessage());
        
       		    //or just use this use this one:
     	       	    $response["success"] = 0;
                    $response["message"] = "Database Error2. Please Try Again!";
                   die(json_encode($response));
                  }
		}
			//exit;
	 }	
?>
<html>
    <head>
        <title>Google Cloud Messaging (GCM) Server in PHP</title>
    </head>
	<body>
	<h1>Register</h1> 
	<form action="gcm.php/?push=1" method="post"> 
	    Username:<br /> 
	    <input type="text" name="regID" value="" /> 
	    <br /><br /> 
	    Password:<br /> 
	    <input type="text" name="Sid" value="" /> 
	    <br /><br /> 
	    <input type="submit" value="Register New User" /> 
	
	
		<h1>Google Cloud Messaging (GCM) Server in PHP</h1>	
	sem:<br /> 
	    <input type="text" name="subcode" value="" /> 
	    <br /><br /> 
	    sec:<br /> 
	    <input type="sec" name="sec" value="" /> 	
		<br /> 	
	class:<br/>
	    <input type="text" name="room" value="" /> 
class:<br/>
	    <input type="text" name="fid" value="" /> 
	    <br />		
			<div>                                
				<textarea rows="2" name="message" cols="23" placeholder="Message to transmit via GCM" ></textarea>
			</div>
			<div><input type="submit"  value="Send Push Notification via GCM" /></div>
		</form>
		<p><h3><?php echo $pushStatus; ?></h3></p>        
    </body>
</html>
