// 로그인 상태 체크
const $isLoggedIn = document.getElementById('isLoggedIn').value;
function isUserLoggedIn() {
    return $isLoggedIn === "true";
}


// 공유하기 버튼 클릭 이벤트
const shareWrap = document.getElementById('shareWrap');
document.querySelector('.sns-share').addEventListener('click',(event) => {
    event.preventDefault();
    if (shareWrap.style.display === 'block') {
        shareWrap.style.display = 'none';
    } else {
        shareWrap.style.display = 'block';
    }
});

// 탭 이동
const $contentData = document.getElementById('contentData');
const $tabList = $contentData.querySelectorAll(':scope > .inner-wrap > .tab-list > ul > li');
    // 초기 상태: 첫 번째 리스트에만 'on' 클래스가 있음
$tabList.forEach((item, index) => {
    if (index === 0) {
        item.classList.add('on');
    } else {
        item.classList.remove('on');
    }
});
    // 이벤트 리스너 추가
$tabList.forEach(item => {
    item.addEventListener('click', event => {
        // <a> 태그가 클릭될 경우에도 <li>가 처리하도록 설정
        event.preventDefault(); // <a> 태그의 기본 동작(링크 이동) 방지
        event.stopPropagation(); // 이벤트 전파 중단

        // 모든 리스트에서 'on' 클래스 제거
        $tabList.forEach(li => li.classList.remove('on'));

        // 클릭된 리스트에 'on' 클래스 추가
        item.classList.add('on');
    });
});


// 관람평 달기
const $reviewList = $contentData.querySelector(':scope > .inner-wrap > .movie-idv-story > .reviewList');
// reviewList 첫 번째, 두 번째 자식 <li> 요소 유지
const $firstReviewItem = $reviewList.querySelector(':scope > li:first-child');
const $wrtReview = $reviewList.querySelector(':scope > li.wrtReview');

// 관람평 쓰기 버튼 클릭 이벤트
// 관람평 실제 작성
const $wrtReviewBox = document.getElementById('wrtReviewBox');
const tooltipLayer = document.getElementById('tooltip-layer');
    // 로그인 했을 때만 가능하도록
if (isUserLoggedIn()) {
    document.querySelector('.wrtReviewBtn').addEventListener('click', (event) => {
        event.preventDefault();
        if ($wrtReviewBox.style.display === 'block') {
            $wrtReviewBox.style.display = 'none';
        } else {
            $wrtReviewBox.style.display = 'block';
        }
    });
}
    // 로그아웃 상태에서는 로그인 바로가기
else if (!isUserLoggedIn()) {
    document.querySelector('.wrtReviewBtn').addEventListener('click', (event) => {
        event.preventDefault();
        if (tooltipLayer.style.display === 'none') {
            tooltipLayer.style.display = 'block';
        } else {
            tooltipLayer.style.display = 'none';
        }
    });
}
    // 로그인 바로가기 서비스
const $loginService =tooltipLayer.querySelector(':scope > .loginTagClick > [rel="login"]')
{
    // 로그인 버튼 클릭했을 때 로그인창과 커버를 보여준다
    $loginService.onclick = (e) => {
        e.preventDefault();
        // 커버 클릭하면 로그인창과 커버 닫기
        $cover.onclick = () => {
            $cover.hide();
            $loginForm.hide();
        }
        // 닫기 버튼 클릭하면 로그인창과 커버 닫기
        $close.onclick = () => {
            $cover.hide();
            $loginForm.hide();
        }
        $cover.show();
        $loginForm.reset(); // 필드 초기화 ( 닫기하고 다시 열면 내용이 남아있어서 )
        $loginForm.show();
        $loginForm['email'].focus();
    }
}


// 관람 키워드 버튼 선택
document.addEventListener("DOMContentLoaded", () => {
    const $keywordButtons = document.querySelectorAll(".keyword-btn");

    $keywordButtons.forEach((button) => {
        button.addEventListener("click", () => {
            // 클릭된 버튼의 활성화 상태를 토글
            button.classList.toggle("active");
        });
    });
});


// 관람평 추가
const appendReview = (review) => {
    const $reviewItem = new DOMParser().parseFromString(`
                            <li class="type01 oneContentTag reviewItem">
                                <div class="story-area">
                                    <div class="user-prof">
                                        <div class="prof-img">
                                            <img th:src="@{/assets/images/mypage/bg-photo.png}" alt="프로필 사진" title="프로필 사진">
                                        </div>
                                        <p class="user-id">${review['nickname']}</p>
                                    </div>
                                    <div class="story-box">
                                        <div class="story-wrap review">
                                            <div class="tit">관람평</div>
                                            <div class="story-cont">
                                                <div class="story-point"><span>${review['score']}</span></div> <!-- 평점 -->
                                                <div class="story-recommend"><em>${review['keyword']}</em></div>          <!-- 키워드 -->
                                                <div class="story-txt">${review['content']}</div>                <!-- 관람평 내용 -->
                                                <div class="story-like">                     <!-- 추천/좋아요 -->
                                                    <button class="oneLikeBtn" title="댓글 추천" type="button">
                                                        <i class="iconset ico-like-gray" th:style="|background-image: url('@{/assets/images/common/ico/ico-like-g.png}')|"></i>
                                                        <span></span> <!-- 추천 수 -->
                                                    </button>
                                                </div>
                                                <div class="story-util">  <!-- 댓글 신고 -->
                                                    <div class="post-function">
                                                        <div class="wrap">
                                                            <button class="btn-alert" type="button" th:style="|background: url('@{/assets/images/common/btn/btn-alert.png}') center no-repeat|">옵션보기</button>
                                                            <div class="balloon-space user">       <!-- 댓글 신고 창 -->
                                                                <div class="balloon-cont">
                                                                    <div class="user">
                                                                        <p class="reset a-c">
                                                                            스포일러 및 욕설/비방하는
                                                                            <br>내용이 있습니까?
                                                                        </p>
                                                                        <button type="button" class="maskOne" data-no="3287240">댓글 신고
                                                                            <i class="iconset ico-arr-right-green" th:style="|background-image: url('@{/assets/images/common/ico/ico-arr-right-green.png}')|"></i>
                                                                        </button>
                                                                    </div>
                                                                    <div class="btn-close">
                                                                        <a href="#" title="닫기">
                                                                            <img th:src="@{/assets/images/common/btn/btn-close-tooltip.png}" alt="닫기">
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>               
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="story-date">
                                    <div><span>${review['createdAt']}</span></div>
                                </div>
                            </li>
`,'text/html').querySelector('li.reviewItem');
    $reviewList.append($reviewItem);
    return $reviewList;
};

// 관람평 불러오기
const loadReview = () => {
    const url = new URL(location.href);
    // 기존 리스트 초기화 (1,2 번째 아이템은 유지)
    $reviewList.innerHTML = '';
    if ($firstReviewItem) {
        $reviewList.append($firstReviewItem);
    }
    if ($wrtReview) {
        $reviewList.append($wrtReview);
    }
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('관람평 정보를 불러오지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        appendReview(JSON.parse(xhr.responseText));
    };
    xhr.open('GET', `../movie-detail?&movieCd=${url.searchParams.get('movieCd')}`);
    xhr.send();
};
loadReview();

const $movieFrom = document.getElementById("movieForm")
const postReview = ($loginForm, $movieForm, $reviewForm) => {
    const url = new URL(location.href);
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('nickname', $loginForm['nickname'].value);
    formData.append('movieCd', $movieForm['movieCd'].value);
    formData.append('content', $reviewForm['content'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('관람평을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        $reviewForm['content'].value = '';
        $reviewForm['content'].focus();
        loadReview();
    };
    xhr.open('POST', '../movie-detail/');
    xhr.send(formData);
};

// 관람평 작성 후 '작성' 버튼을 클릭하였을 때
const $reviewForm = document.getElementById("reviewForm");
$reviewForm.onsubmit = (e) => {
    e.preventDefault();
    postReview($loginForm, $movieFrom, $reviewForm);
}

// 실관람평 탭으로 이동
