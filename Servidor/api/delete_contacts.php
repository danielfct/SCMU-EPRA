<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


// include database and object file
include_once './config/database.php';
include_once './objects/user.php';

// get database connection
$database = new Database();
$db = $database->getConnection();

// prepare simulator object
$user = new User($db);

$data = json_decode(file_get_contents("php://input"));

$user->email = $data->email;

// delete simulator
if($user->delete()){
	echo '{';
		echo '"message": "User was deleted.", "success": "1"';
	echo '}';
}

// if unable to delete the simulator
else{
	http_response_code(404);
	echo '{';
		echo '"message": "Unable to delete user.", "success": "0"';
	echo '}';
}
?>
