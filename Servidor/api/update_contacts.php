<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once './config/database.php';

include_once './objects/user.php';

$database = new Database();
$db = $database->getConnection();

$user = new User($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

$user->email = $data->email;
if (property_exists($data, 'privilegios')) {
  $user->privilegios = $data->privilegios;
}
if (property_exists($data, 'nome')) {
  $user->nome = $data->nome;
}
if (property_exists($data, 'telemovel')) {
  $user->telemovel = $data->telemovel;
}

if($user->update()){
    echo '{';
        echo '"message": "User updated successfully!", "success": "1"';
    echo '}';
}

else{
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to update user.", "success": "0"';
    echo '}';
}
?>
