{
    const $registerForm = document.getElementById('registerForm');

    $registerForm.onsubmit = (e) => {
        e.preventDefault();

        const xhr = new XMLHttpRequest();
        const url = new URL(location.href);
        url.pathname = '/success';
        const formData = new FormData();
        formData.append('email', $registerForm['email'].value);
        formData.append('password', $registerForm['password'].value);
        formData.append('nickname', $registerForm['nickname'].value);
        formData.append('contact', $registerForm['contact'].value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            Loading.hide();
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('요청을 전송하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            if (response['results'] === 'success') {
                alert('회원가입해 주셔서 감사합니다. 입력하신 이메일로 계정을 인증할 수 있는 링크를 전송하였습니다. 계정 인증 후 로그인할 수 있으며, 해당 링크는 24시간 동안만 유효하니 유의해 주세요.');
                location.href = '/success';
            } else if (response['results'] === 'failure_duplicate_contact') {
                alert(`입력하신 연락처(${$registerForm['contact'].value})는 이미 사용 중입니다. 다른 연락처를 사용해 주세요.`);
            } else if (response['results'] === 'failure_duplicate_email') {
                alert(`입력하신 이메일(${$registerForm['email'].value})은 이미 사용 중입니다. 다른 이메일을 사용해 주세요.`);
            } else if (response['results'] === 'failure_duplicate_nickname') {
                alert(`입력하신 닉네임(${$registerForm['nickname'].value}}은 이미 사용 중입니다. 다른 이메일을 사용해 주세요.`);
            } else {
                alert(`알 수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요.`);
            }
        };
        xhr.open('POST', '../user/');
        xhr.send(formData);
        Loading.show(0);
    };
}
