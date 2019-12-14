<?php
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
   public function storeUser($sid,$password,$deviceid) {
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt*/
 
        $stmt = $this->conn->prepare("INSERT INTO login(sid, password, deviceid, logged_at, salt) VALUES(?, ?, ?, NOW(), ?)");
        $stmt->bind_param("ssss", $sid,$encrypted_password,$deviceid,$salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM student_details WHERE Sid = ?");
            $stmt->bind_param("s", $sid);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getmailid($sid) {
 
        $stmt = $this->conn->prepare("SELECT *  FROM student_details WHERE Sid = ?");
 
        $stmt->bind_param("s", $sid);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
             return $user;
            
        }
    }
 
    /**
     * Check user is existed or not
     */
   public function isUserExisted($sid) {
        $stmt = $this->conn->prepare("SELECT Sid from student_details WHERE Sid = ?");
         $stmt->bind_param("s", $sid);
         $stmt->execute();
         $stmt->store_result();
         if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
	public function login_status($sid)
{
$stmt = $this->conn->prepare("SELECT * from login WHERE sid = ?");
         $stmt->bind_param("s", $sid);
         $stmt->execute();
         if ( $stmt->execute()) {
            // user existed
	    $user = $stmt->get_result()->fetch_assoc();
	    $stmt->close();
	    return $user;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
}
public function validate_password($sid,$password)
{
$stmt = $this->conn->prepare("SELECT * from login where sid = ?");
$stmt->bind_param("s",$sid);
if($stmt->execute()) {
$pws = $stmt->get_result()->fetch_assoc();
$stmt->close();
$salt=$pws['salt'];
$enc_pwd = $pws['password'];
$hash = $this->checkhashSSHA($salt,$password);
if($enc_pwd==$hash)
{
$stmt = $this->conn->prepare("SELECT *  FROM student_details WHERE Sid = ?");
 	$stmt->bind_param("s", $sid);
 	if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $user;
}
} 
}
}
public function userdetails($sid)
{
$stmt = $this->conn->prepare("SELECT *  FROM student_details WHERE Sid = ?");
 	$stmt->bind_param("s", $sid);
 	if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $user;
}
}
public function delete_entry($sid)
{
$stmt = $this->conn->prepare("DELETE FROM login WHERE sid = ?");
$stmt->bind_param("s",$sid);
$stmt->execute();
$stmt->close();
}
public function getmarks($sid,$ia) {
$stmt = $this->conn->prepare("SELECT * from ia_asmt where sid = ? and ia_no = ?");
$stmt->bind_param("ss",$sid,$ia);
if($stmt->execute())
{
            $marks = $stmt->get_result()->fetch_assoc();
	    $stmt->close();
return $marks;
} else {
return NULL;}
}
public function getsubjects($sem,$dept) {
$stmt = $this->conn->prepare("SELECT * from subject where sem = ? and dept = ?");
$stmt->bind_param("ss",$sem,$dept);
$stmt->execute();
$subjects = $stmt->get_result()->fetch_assoc();
            $stmt->close();
return $subjects;

}
public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
}
?>
