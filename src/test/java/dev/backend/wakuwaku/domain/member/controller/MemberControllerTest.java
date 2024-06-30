package dev.backend.wakuwaku.domain.member.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import dev.backend.wakuwaku.domain.member.dto.request.MemberUpdateRequest;
import dev.backend.wakuwaku.domain.member.entity.Member;
import dev.backend.wakuwaku.domain.member.service.MemberService;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.domain.oauth.dto.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/wakuwaku/v1/members";

    private Member member;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        member = Member.builder()
                .oauthServerId("oauth-id")
                .oauthServerType(OauthServerType.GOOGLE)
                .email("test@example.com")
                .birthday("1990-01-01")
                .nickname("nickname")
                .profileImageUrl("http://example.com/profile.jpg")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("회원 ID로 조회 테스트")
    void findById() throws Exception {
        // given
        given(memberService.findById(1L)).willReturn(member);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))  // 응답 코드 추가
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))  // 응답 메시지 추가
                .andExpect(jsonPath("$.data.email").value(member.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(member.getNickname()))
                .andDo(MockMvcRestDocumentationWrapper.document("get-member-by-id",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member")
                                .description("회원 ID로 회원 정보를 조회합니다.")
                                .pathParameters(parameterWithName("id").description("회원 ID"))
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data.oauthServerId").type(JsonFieldType.STRING).description("OAuth 서버 ID"),
                                        fieldWithPath("data.oauthServerType").type(JsonFieldType.STRING).description("OAuth 서버 타입"),
                                        fieldWithPath("data.birthday").type(JsonFieldType.STRING).description("회원 생일"),
                                        fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.role").type(JsonFieldType.STRING).description("회원 역할")
                                )
                                .build()
                        )
                ));

        then(memberService).should().findById(1L);
    }

    @Test
    @DisplayName("회원 정보 업데이트 테스트")
    void update() throws Exception {
        // given
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("newNickname", "http://example.com/new-profile.jpg", "1991-01-01");
        given(memberService.update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(MemberUpdateRequest.class))).willReturn(1L);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nickname\": \"newNickname\", \"profileImageUrl\": \"http://example.com/new-profile.jpg\", \"birthday\": \"1991-01-01\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))  // 응답 코드 추가
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))  // 응답 메시지 추가
                .andExpect(jsonPath("$.data.id").value(1L))
                .andDo(MockMvcRestDocumentationWrapper.document("update-member",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member")
                                .description("회원 정보를 업데이트합니다.")
                                .pathParameters(parameterWithName("id").description("회원 ID"))
                                .requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("새로운 닉네임"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("새로운 프로필 이미지 URL"),
                                        fieldWithPath("birthday").type(JsonFieldType.STRING).description("새로운 생일")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID")
                                ).build()
                        )
                ));

        then(memberService).should().update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(MemberUpdateRequest.class));
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void delete() throws Exception {
        // given
        Long memberId = 1L;

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL + "/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))  // 응답 코드 추가
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))  // 응답 메시지 추가
                .andDo(MockMvcRestDocumentationWrapper.document("delete-member",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member")
                                .description("회원 정보를 삭제합니다.")
                                .pathParameters(parameterWithName("id").description("회원 ID"))
                                .build()
                        )
                ));

        then(memberService).should().deactivateById(memberId);
    }

    @Test
    @DisplayName("회원 목록 조회 테스트")
    void findAll() throws Exception {
        // given
        List<Member> members = new ArrayList<>();
        members.add(member);
        given(memberService.findAll()).willReturn(members);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))  // 응답 코드 추가
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))  // 응답 메시지 추가
                .andExpect(jsonPath("$.data[0].email").value(member.getEmail()))
                .andExpect(jsonPath("$.data[0].nickname").value(member.getNickname()))
                .andDo(MockMvcRestDocumentationWrapper.document("get-member-list",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member")
                                .description("회원 목록을 조회합니다.")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data[].oauthServerId").type(JsonFieldType.STRING).description("OAuth 서버 ID"),
                                        fieldWithPath("data[].oauthServerType").type(JsonFieldType.STRING).description("OAuth 서버 타입"),
                                        fieldWithPath("data[].birthday").type(JsonFieldType.STRING).description("회원 생일"),
                                        fieldWithPath("data[].profileImageUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data[].role").type(JsonFieldType.STRING).description("회원 역할")
                                ).build()
                        )
                ));

        then(memberService).should().findAll();
    }
}
