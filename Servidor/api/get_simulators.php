<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// include database and object files
include_once './config/database.php';
include_once './objects/simulator.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$simulator = new Simulator($db);

$stmt = $simulator->read(isset($_GET['search']) ? $_GET['search'] : NULL);
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // products array
    $simulator_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $simulator_item=array(
            "id" => $id,
            "nome" => $nome,
            "estadoAtual" => $estadoAtual,
            "areaId" => $areaId,
        );

        array_push($simulator_arr, $simulator_item);
    }

    echo json_encode($simulator_arr);
}

else{
    http_response_code(404);
    echo json_encode(
        array("message" => "No simulators found.")
    );
}
?>
