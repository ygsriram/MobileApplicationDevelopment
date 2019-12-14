<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$fid = $_POST["fid"];
$subcode = $_POST["subcode"];
$hcot = $db->getheadcount($fid,$subcode);
if($hcot){
$response["error"]=FALSE;
if($hcot["count(*)"]==0){
$response["add"]=0;
$response["error_msg"]="No feedback Received";}
else{
$abc = $hcot["count(*)"];
$response["add"]=1;
$response["error_msg"]="Total head count: $abc";
}
echo json_encode($response);

}
else
{
$response["error"]=FALSE;
$response["add"]=0;
$response["error_msg"]="No feedback Received";
echo json_encode($response);
}
?>

