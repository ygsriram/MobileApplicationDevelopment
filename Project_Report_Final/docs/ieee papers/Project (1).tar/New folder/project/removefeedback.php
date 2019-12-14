<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$fid = $_POST["fid"];
$subcode = $_POST["subcode"];
$db->removefeedback($fid,$subcode);
$response["error"]=FALSE;
$response["error_msg"]="Deleted";
echo json_encode($response);
?>
