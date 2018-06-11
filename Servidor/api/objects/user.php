<?php
class User {

    // database connection and table name
    private $conn;
    private $table_name;

    // object properties
    public $nome;
    public $telemovel;
    public $email;
    public $password;
    public $admin;
    public $privilegios;
    public $pin;

    // constructor with $db as database connection
    public function __construct($db) {
        $this->conn = $db;
        $this->table_name = "utilizador";
    }

    // read users
    function read($filter) {
        if ($filter) {
            $query = "SELECT * FROM $this->table_name WHERE email = ? LIMIT 1";
        } else {
            $query = "SELECT * FROM $this->table_name";
        }
        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // bind
        if ($filter) {
            $filter=htmlspecialchars(strip_tags($filter));
            $stmt->bindParam(1, $filter);
        }
        
        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create user
    function create() {

        // query to insert record
        $query = "INSERT INTO $this->table_name SET
                    nome=:nome,
                    telemovel=:telemovel,
                    email=:email,
                    password=:password,
                    admin=:admin,
                    privilegios=:privilegios,
                    pin=:pin";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->telemovel=htmlspecialchars(strip_tags($this->telemovel));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->password=htmlspecialchars(strip_tags($this->password));
        $this->admin=htmlspecialchars(strip_tags($this->admin));
        $this->privilegios=htmlspecialchars(strip_tags($this->privilegios));
        $this->pin=htmlspecialchars(strip_tags($this->pin));

        // bind values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":telemovel", $this->telemovel);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":password", $this->password);
        $stmt->bindParam(":admin", $this->admin);
        $stmt->bindParam(":privilegios", $this->privilegios);
        $stmt->bindParam(":pin", $this->pin);

        // execute query
        $stmt->execute();
        
        return $stmt;
    }

    function delete() {

        // delete query
        $query = "DELETE FROM $this->table_name WHERE email = ?";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->email=htmlspecialchars(strip_tags($this->email));

        // bind id of record to delete
        $stmt->bindParam(1, $this->email);

        try {
          // execute query
          if($stmt->execute()){
              return true;
          }
        } catch(PDOException $e) {
        }

        return false;

    }

    function addComma($res) {
      if(strlen($res) > 0) {
        $res .= ", ";
      }
      return $res;
    }

    function buildQueryAttributes() {
      $res = "";
      if($this->nome) {
        $res .= "nome = :nome";
      }
      if($this->telemovel) {
        $res = $this->addComma($res);
        $res .= "telemovel = :telemovel";
      }
      if($this->password) {
        $res = $this->addComma($res);
        $res .= "password = :password";
      }
      if($this->admin) {
        $res = $this->addComma($res);
        $res .= "admin = :admin";
      }
      if($this->privilegios) {
        $res = $this->addComma($res);
        $res .= "privilegios = :privilegios";
      }
      if($this->pin) {
        $res = $this->addComma($res);
        $res .= "pin = :pin";
      }

      return $res;
    }

    function update() {

      $query = "UPDATE $this->table_name SET " . $this->buildQueryAttributes() . " WHERE email = ?";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        if($this->nome) {
          $this->nome=htmlspecialchars(strip_tags($this->nome));
          $stmt->bindParam(':nome', $this->nome);
        }
        if($this->telemovel) {
          $this->telemovel=htmlspecialchars(strip_tags($this->telemovel));
          $stmt->bindParam(':telemovel', $this->telemovel);
        }
        if($this->email) {
          $this->email=htmlspecialchars(strip_tags($this->email));
          $stmt->bindParam(':email', $this->email);
        }
        if($this->password) {
          $this->password=htmlspecialchars(strip_tags($this->password));
          $stmt->bindParam(':password', $this->password);
        }
        if($this->admin) {
          $this->admin=htmlspecialchars(strip_tags($this->admin));
          $stmt->bindParam(':admin', $this->admin);
        }
        if($this->privilegios) {
          $this->privilegios=htmlspecialchars(strip_tags($this->privilegios));
          $stmt->bindParam(':privilegios', $this->privilegios);
        }
        if($this->pin) {
          $this->pin=htmlspecialchars(strip_tags($this->pin));
          $stmt->bindParam(':pin', $this->pin);
        }

        $stmt->bindParam(1, $this->email);

        // execute the query

          try {
            return $stmt->execute();
          } catch(PDOException $e) {
          }


        return false;
    }
}
