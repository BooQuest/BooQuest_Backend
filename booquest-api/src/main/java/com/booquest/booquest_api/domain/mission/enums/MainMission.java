package com.booquest.booquest_api.domain.mission.enums;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MainMission {
    FIRST(1, "채널 세팅", "채널을 개설하고 기본 세팅을 완료한 후 방향성을 기획한다",
            List.of("채널 카테고리·주제 기획", "타깃 정의 및 업로드 플랫폼 선정", "계정 개설 및 프로필 세팅", "벤치마킹할 채널/계정 분석", "콘텐츠 제작에 필요한 준비물과 제작 방법 점검"),
            List.of("직장인 재테크, 대학생 브이로그, 육아 용품 소개와 같이 주제가 명확하면 좋습니다.", "메인 플랫폼을 우선 정하고, 보조 홍보용으로 함께 운영할 한두 개의 플랫폼도 추가하면 좋습니다.",
                    "닉네임, 프로필 사진, 채널명, 소개글, 배너 등 계정 개설 후 프로필 세팅을 완료하세요.", "성공 사례 채널들을 분석하고, 인기 콘텐츠의 특징을 파악한 뒤 차별화 포인트를 기획하세요.",
                    "콘텐츠 제작에 필요한 준비물들을 서치하고 준비하세요. 예 : 편집 툴, 촬영용품 등")),
    SECOND(2, "초기 콘텐츠 확보", "처음 5개의 콘텐츠를 기획, 제작, 업로드한다",
            List.of("주제에 맞는 콘텐츠 아이디어 기획 및 구체화", "콘텐츠 제작 과정 루틴 및 업로드 일정 설정", "5개의 구체적인 기획 및 대본(내용) 작성",
                    "경쟁 채널 콘텐츠와 비교·분석 및 퀄리티 업그레이드", "앞선 단계들을 참고하며 5개의 콘텐츠 업로드 완료"),
            List.of("선정한 주제와 타깃을 기준으로 최신 트렌드를 반영한 아이디어를 짜고 내용을 구체화해보세요.", "기획→제작→업로드 루틴을 짜고, 주기적 업로드 일정을 계획하세요.",
                    "각 콘텐츠의 흐름, 핵심 메시지, 전달 방식 등을 꼼꼼히 신경 써서 준비하세요. 경쟁사 채널을 참고하면 좋습니다.","유사 채널의 인기 콘텐츠를 분석해 제목·구성·비주얼 등을 비교하고, 내 콘텐츠 시안에 반영해 개선하세요",
                    "앞선 단계들을 지속적으로 진행하며 최종 점검을 거친 뒤 업로드하세요.")),
    THIRD(3, "유입 활동", "10개의 콘텐츠를 업로드하며 초기 시청자 유입 활동을 실행한다",
            List.of("제작한 컨텐츠를 커뮤니티·SNS에 공유", "다른 플랫폼·채널 동시 업로드", "댓글 소통, 크리에이터 교류 활동 강화", "트렌드를 분석하고 적용하며, 10개의 구체적인 콘텐츠 기획 및 대본(내용) 작성"
            , "앞선 단계들을 참고하며 10개의 콘텐츠 업로드 완료"),
            List.of("카페, 사이트, 오픈채팅방, 지인 네트워크, SNS 스토리 등 외부 채널에 적극적으로 공유하세요.", "유튜브/블로그/인스타/쇼츠/릴스 등 여러 플랫폼에 맞게 재가공·동시 업로드해 노출 범위를 넓히세요.",
                    "댓글 작업을 통해 소통과 반응을 늘리고, 비슷한 분야의 활동가와 팔로우·친구 맺기를 하며 상호작용을 확대하세요.", "트렌드 반영 여부, 차별화 포인트, 전달 방식 등을 꼼꼼히 반영해 기획하세요.",
                    "앞선 단계들을 지속적으로 진행하며 최종 점검을 거친 뒤 업로드하세요.")),
    FOURTH(4, "채널 영향력 강화", "20개의 콘텐츠를 업로드하며 콘텐츠 품질과 채널 영향력을 강화한다",
            List.of("반응 좋은 콘텐츠 분석 및 컨텐츠 강점 강화", "시청자 피드백 반영","적극적으로 소통하며 콘텐츠 품질 개선", "채널 강점 분석을 반영하여 20개의 구체적인 콘텐츠 기획 및 대본(내용) 작성",
                    "앞선 단계들을 참고하며 20개의 콘텐츠 업로드 완료"),
            List.of("업로드한 콘텐츠 중 조회수·참여율이 높은 콘텐츠 유형을 분석해 내 채널의 강점을 강화하세요.", "댓글, DM, 반응 패턴을 분석해 개선 포인트를 다음 콘텐츠에 반영하세요.",
                    "팔로워 및 크리에이터와 적극적으로 소통하며 채널 활성도를 늘리고, 콘텐츠의 세부 요소를 점검하고 개선하세요.", "앞서 분석한 채널의 강점을 반영하고, 피드백을 적용해 더욱 완성도 있는 기획을 하세요.",
                    "앞선 단계들을 지속적으로 진행하며 최종 점검을 거친 뒤 업로드하세요.")),
    FIFTH(5, "수익화 시도", "20개의 콘텐츠를 업로드하며 브랜드 제휴·광고·협찬·구독 등 수익화를 시도한다",
            List.of("채널과 관련성 높은 브랜드·업체 조사", "운영중인 플랫폼 주요 수익화 방식 파악", "20개의 구체적인 콘텐츠 기획 및 대본(내용) 작성",
                    "앞선 단계들을 참고하며 20개의 콘텐츠 업로드 완료",
                    "수익화·협찬 시도"),
            List.of("컨텐츠의 주제와 관련 있고 협찬·광고 가능성이 있는 업체나 브랜드를 조사하세요.", "운영중인 플랫폼의 주요 수익원·수익화 및 광고·협찬 방법을 파악하세요.",
                    "브랜드와 연계될 수 있거나 전문성을 보여줄 수 있는 기획을 반영하세요.", "앞선 단계들을 지속적으로 진행하며 최종 점검을 거친 뒤 업로드하세요",
                    "최소 5곳 이상의 기업에 협찬·광고 제안서를 제작해 전송하는 등 각 플랫폼의 수익화 방식을 실제로 적용해보세요. 당장 성과가 없더라도 이러한 경험은 채널의 방향성과 콘텐츠 성장에 큰 도움이 됩니다."));

    private final int orderNo;
    private final String title;
    private final String designNotes;
    private final List<String> missionSteps;
    private final List<String> guides;

    public static String getTitleByOrderNo(int orderNo) {
        for (MainMission mainMission : MainMission.values()) {
            if (mainMission.orderNo == orderNo) {
                return mainMission.title;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 단계입니다.");
    }

    public static String getDesignNotesByOrderNo(int orderNo) {
        for (MainMission mainMission : MainMission.values()) {
            if (mainMission.orderNo == orderNo) {
                return mainMission.designNotes;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 단계입니다.");
    }

    public static String getGuideByOrderNo(int orderNo) {
        for (MainMission mainMission : MainMission.values()) {
            if (mainMission.orderNo == orderNo) {
                return mainMission.guides.stream()
                        .map((guide) -> (mainMission.guides.indexOf(guide) + 1) + ". " + guide)
                        .collect(Collectors.joining("\n"));
            }
        }
        throw new IllegalArgumentException("존재하지 않는 단계입니다.");
    }

    public static String getMissionSteps(int orderNo, int missionOrderNo) {
        for (MainMission mainMission : MainMission.values()) {
            if (mainMission.orderNo == missionOrderNo) {
                return mainMission.missionSteps.get(orderNo - 1);
            }
        }
        throw new IllegalArgumentException("존재하지 않는 단계입니다.");
    }
}
