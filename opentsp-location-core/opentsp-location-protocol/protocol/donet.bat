CD E:\workspace\2.0\base\LCProtocol\protocol
E:
protoc --descriptor_set_out=output/donet/AreaType.protobin --include_imports core\proto\common\net\AreaType.proto
protogen -output_directory=output/donet/ output/donet/AreaType.protobin
protoc --descriptor_set_out=output/donet/RegularCode.protobin --include_imports core\proto\common\net\RegularCode.proto
protogen -output_directory=output/donet/ output/donet/RegularCode.protobin
protoc --descriptor_set_out=output/donet/ShortLocationData.protobin --include_imports core\proto\common\net\ShortLocationData.proto
protogen -output_directory=output/donet/ output/donet/ShortLocationData.protobin
protoc --descriptor_set_out=output/donet/AreaSpeeding.protobin --include_imports core\proto\dataaccess\common\net\AreaSpeeding.proto
protogen -output_directory=output/donet/ output/donet/AreaSpeeding.protobin
protoc --descriptor_set_out=output/donet/DoorOpenOutArea.protobin --include_imports core\proto\dataaccess\common\net\DoorOpenOutArea.proto
protogen -output_directory=output/donet/ output/donet/DoorOpenOutArea.protobin
protoc --descriptor_set_out=output/donet/DriverNotCard.protobin --include_imports core\proto\dataaccess\common\net\DriverNotCard.proto
protogen -output_directory=output/donet/ output/donet/DriverNotCard.protobin
protoc --descriptor_set_out=output/donet/DrivingBan.protobin --include_imports core\proto\dataaccess\common\net\DrivingBan.proto
protogen -output_directory=output/donet/ output/donet/DrivingBan.protobin
protoc --descriptor_set_out=output/donet/InOutArea.protobin --include_imports core\proto\dataaccess\common\net\InOutArea.proto
protogen -output_directory=output/donet/ output/donet/InOutArea.protobin
protoc --descriptor_set_out=output/donet/RegularData.protobin --include_imports core\proto\dataaccess\common\net\RegularData.proto
protogen -output_directory=output/donet/ output/donet/RegularData.protobin
protoc --descriptor_set_out=output/donet/RouteDriverTime.protobin --include_imports core\proto\dataaccess\common\net\RouteDriverTime.proto
protogen -output_directory=output/donet/ output/donet/RouteDriverTime.protobin
protoc --descriptor_set_out=output/donet/QueryRegularDataRes.protobin --include_imports core\proto\dataaccess\regular\net\QueryRegularDataRes.proto
protogen -output_directory=output/donet/ output/donet/QueryRegularDataRes.protobin
