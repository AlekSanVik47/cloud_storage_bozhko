package services;

import dto.response.AuthResponse;
import io.grpc.stub.StreamObserver;
import ru.cloudstorage.cloud_storage_core.dto.UserDto;

public class AuthProtoService extends AuthServiceGrpc.AuthServiceImplBase {
    @Override
    public void register(UserDto request, StreamObserver<AuthResponse> responseObserver) {

        String userName = request.getUserName();
        String password = request.getPassword();
        String email = request.getEmail();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String matchingPassword = request.getMatchingPassword();

        if (!password.equals(matchingPassword)) {
            responseObserver.onError(new RuntimeException("Пароли не совпадают"));
            return;
        }

        responseObserver.onNext(AuthResponse.builder()
                .username(request.getUserName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(UserDto request, StreamObserver<AuthResponse> responseObserver) {
        // Логика авторизации
        responseObserver.onNext(AuthResponse.builder()
                .username(request.getUserName())
                .build());
        responseObserver.onCompleted();
    }
}
