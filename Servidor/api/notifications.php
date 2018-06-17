<?php
include_once './config/database.php';
include_once './objects/notification.php';
include_once './objects/tracking.php';

$database = new Database();
$db = $database->getConnection();
$device_tokens = new Notification($db);

$areaEntrada = $_GET['areaEntrada'];

if ($_GET['areaEntrada']) {
    $tracking = new Tracking($db);
    
      $tracking->areaAtual = $areaEntrada ;
      $tracking->areaEntrada = $areaEntrada ;
      
        $stmt = $db->prepare("SELECT COUNT(*) AS num FROM utilizador");
        $stmt->execute();
        $num = $stmt->rowCount();
            
        if ($num > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            $tracking->pessoasNotificadas = $row['num'];
        } else {
            $tracking->pessoasNotificadas = 2;
        }
    
    if($tracking->create()){
        $stmt = $db->prepare("SELECT id FROM tracking ORDER BY id DESC LIMIT 1");
        $stmt->execute();
        $num = $stmt->rowCount();
        
        if ($num > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            $id = $row['id'];
            
            $stmt = $device_tokens->read();
            $num = $stmt->rowCount();
            
            if ($num > 0) {
                $device_arr = array();
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
                    extract($row);
                    array_push($device_arr, $token);
                }
            
                define('API_ACCESS_KEY', 'AAAA6ekUT6A:APA91bHUHB-AsP_N5Tz4MN7jMtbYQQgVTEZN-4f-3TzXe0S7BLcMCso578SqXchgzdKXA7xqIXDJ_WhnPCv1H2dTuJGWBAC_c31uRaF5Vmotr4j0jS71gGQBm-gTiji-lv0vYxfoMgPk'); // API access key from Firebase Console
            
                $msg = array
                (
                    'text'  => 'A burglar broke into your house!',
                    'id' => $id,
                    'title' => 'WARNING',
                    'content' => 'A burglar broke into your house!'
                );
                
                echo json_encode($msg) . '<br />';
            
                $fields = array
                (
                    'registration_ids'    => $device_arr,
                    'data' => $msg
                );
            
                $headers = array
                (
                    'Authorization: key=' . API_ACCESS_KEY,
                    'Content-Type: application/json',
                );
            
                $ch = curl_init();
                curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
                curl_setopt( $ch,CURLOPT_POST, true );
                curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
                curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
                curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
                curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ));
                $result = curl_exec($ch );
                curl_close( $ch );
                echo $result;
            
            } else {
              echo "Error: No tokens.";
            }
            
        } else {
            echo "Unable to select tracking id";
        }

      // execute query
      $stmt->execute();
    } else {
        echo "Unable to create tracking.";
    }
}

?>
