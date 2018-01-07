namespace java com.twitter.finagle.example.thriftjava
#@namespace scala com.twitter.finagle.example.thriftscala

struct TLogObjRequest{
1:string message,
2:i32 logLevel
}

struct TLogObjResponse{
1:i32 respCode,
2:string respDesc
}

exception WriteException {}
exception ReadException {}

service LoggerService {
  string log(1: string message, 2: i32 logLevel) throws (1: WriteException writeEx);
  TLogObjResponse logObj(1: TLogObjRequest request) throws (1: WriteException writeEx);
  i32 getLogSize() throws (1: ReadException readEx);
}


