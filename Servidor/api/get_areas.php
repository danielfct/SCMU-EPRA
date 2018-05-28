<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// include database and object files
include_once './config/database.php';
include_once './objects/area.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$area = new Area($db);

$stmt = $area->read(isset($_GET['id']) ? $_GET['id'] : NULL);
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // products array
    $area_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $area_item=array(
            "id" => $id,
            "nome" => $nome,
            "alarmeLigado" => $alarmeLigado,
            "sensor" => $sensor,
        );

        /*if ($num == 1) {
          echo json_encode($area_item);
          return;
        }*/

        array_push($area_arr, $area_item);
    }

    echo json_encode($area_arr);
}

else{
    http_response_code(404);
    echo json_encode(
        array("message" => "No areas found.")
    );
}
?>
