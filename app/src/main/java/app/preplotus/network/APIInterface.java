package app.preplotus.network;


import static app.preplotus.utilities.Constants.API_ADD_EXAM_PREFERENCES;
import static app.preplotus.utilities.Constants.API_APPLY_COUPON;
import static app.preplotus.utilities.Constants.API_CHANGE_PASSWORD;
import static app.preplotus.utilities.Constants.API_CHECK_REFERRAL_CODE;
import static app.preplotus.utilities.Constants.API_COINS_HISTORY;
import static app.preplotus.utilities.Constants.API_CREATE_ORDER;
import static app.preplotus.utilities.Constants.API_FETCH_ALL_NOTES;
import static app.preplotus.utilities.Constants.API_FETCH_ALL_PACKAGES;
import static app.preplotus.utilities.Constants.API_FETCH_APP_DETAILS;
import static app.preplotus.utilities.Constants.API_FETCH_COMPAREDATA;
import static app.preplotus.utilities.Constants.API_FETCH_CONTENT;
import static app.preplotus.utilities.Constants.API_FETCH_DASHBOARD;
import static app.preplotus.utilities.Constants.API_FETCH_HOME;
import static app.preplotus.utilities.Constants.API_FETCH_MY_RESULTS;
import static app.preplotus.utilities.Constants.API_FETCH_PROFILE;
import static app.preplotus.utilities.Constants.API_FETCH_QUESTIONS;
import static app.preplotus.utilities.Constants.API_FETCH_SCOREBOARD;
import static app.preplotus.utilities.Constants.API_FETCH_SOLUTIONS;
import static app.preplotus.utilities.Constants.API_FETCH_SUBJECT_TOPICS;
import static app.preplotus.utilities.Constants.API_FETCH_SUBSCRIPTIONS_HISTORY;
import static app.preplotus.utilities.Constants.API_FETCH_SUBSCRIPTION_DETAILS;
import static app.preplotus.utilities.Constants.API_FETCH_SUB_TOPICS;
import static app.preplotus.utilities.Constants.API_FETCH_SUPER_GROUPS;
import static app.preplotus.utilities.Constants.API_FETCH_TESTS;
import static app.preplotus.utilities.Constants.API_FETCH_TEST_INSTRUCTIONS;
import static app.preplotus.utilities.Constants.API_FETCH_TOPICS;
import static app.preplotus.utilities.Constants.API_FORGOT_PASSWORD;
import static app.preplotus.utilities.Constants.API_GET_CATEGORIES;
import static app.preplotus.utilities.Constants.API_GET_CONTACT_US;
import static app.preplotus.utilities.Constants.API_LOGIN;
import static app.preplotus.utilities.Constants.API_LOGOUT_USER;
import static app.preplotus.utilities.Constants.API_MY_EXAM_PREFERENCES;
import static app.preplotus.utilities.Constants.API_NOTIFICATION_MANAGE;
import static app.preplotus.utilities.Constants.API_REDEEM_COINS;
import static app.preplotus.utilities.Constants.API_REGISTER;
import static app.preplotus.utilities.Constants.API_REMOVE_PREFERENCES;
import static app.preplotus.utilities.Constants.API_RESET_PASSWORD;
import static app.preplotus.utilities.Constants.API_SOCIAL_LOGIN;
import static app.preplotus.utilities.Constants.API_SUBMIT_TEST;
import static app.preplotus.utilities.Constants.API_UPDATE_PAYMENT;
import static app.preplotus.utilities.Constants.API_UPDATE_PROFILE;

import app.preplotus.model.CategoryDataResponse;
import app.preplotus.model.CompareResponse;
import app.preplotus.model.DashboardResponse;
import app.preplotus.model.ForgotPasswordResponse;
import app.preplotus.model.GeneralResponse;
import app.preplotus.model.LoginSignupResponse;
import app.preplotus.model.MyCoinsResponse;
import app.preplotus.model.MyExamPreferencesResponse;
import app.preplotus.model.MyResultsResponse;
import app.preplotus.model.NotesResponse;
import app.preplotus.model.PackagesResponse;
import app.preplotus.model.QuestionListResponse;
import app.preplotus.model.ScorecardResponse;
import app.preplotus.model.SolutionResponse;
import app.preplotus.model.SubTopicsResponse;
import app.preplotus.model.SubjectTopicsResponse;
import app.preplotus.model.SubscriptionResponse;
import app.preplotus.model.SuperGroupResponse;
import app.preplotus.model.TestsResponse;
import app.preplotus.model.TestsTabResponse;
import app.preplotus.model.TopicsResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIInterface {

    @POST(API_REGISTER)
    @FormUrlEncoded
    Call<LoginSignupResponse> apiRegister(@FieldMap Map<String, String> params);

    @POST(API_REGISTER)
    @FormUrlEncoded
    Call<ResponseBody> apiRegister2(@FieldMap Map<String, String> params);

    @POST(API_LOGIN)
    @FormUrlEncoded
    Call<LoginSignupResponse> apiLogin(@FieldMap Map<String, String> params);

    @POST(API_SOCIAL_LOGIN)
    @FormUrlEncoded
    Call<LoginSignupResponse> apiSocialLogin(@FieldMap Map<String, String> params);

    @POST(API_FORGOT_PASSWORD)
    @FormUrlEncoded
    Call<ForgotPasswordResponse> apiForgortPassword(@FieldMap Map<String, String> params);

    @POST(API_RESET_PASSWORD)
    @FormUrlEncoded
    Call<GeneralResponse> apiResetPassword(@FieldMap Map<String, String> params);

    @POST(API_CHANGE_PASSWORD)
    @FormUrlEncoded
    Call<GeneralResponse> apiChangePassword(@FieldMap Map<String, String> params);

    @POST(API_GET_CATEGORIES)
    @FormUrlEncoded
    Call<CategoryDataResponse> apiGetCategories(@FieldMap Map<String, String> params);

    @POST(API_GET_CONTACT_US)
    @FormUrlEncoded
    Call<GeneralResponse> apiContactUs(@FieldMap Map<String, String> params);

    @POST(API_LOGOUT_USER)
    @FormUrlEncoded
    Call<GeneralResponse> apiLogout(@FieldMap Map<String, String> params);

    @POST(API_NOTIFICATION_MANAGE)
    @FormUrlEncoded
    Call<GeneralResponse> apiNotification(@FieldMap Map<String, String> params);

    @POST(API_FETCH_PROFILE)
    @FormUrlEncoded
    Call<LoginSignupResponse> apiFetchProfile(@FieldMap Map<String, String> params);

    @Multipart
    @POST(API_UPDATE_PROFILE)
    Call<LoginSignupResponse> updateProfile(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part body);

    @POST(API_FETCH_APP_DETAILS)
    @FormUrlEncoded
    Call<ResponseBody> fetchAppDetails(@FieldMap Map<String, String> params);

    @POST(API_FETCH_ALL_NOTES)
    @FormUrlEncoded
    Call<NotesResponse> fetchAllNotes(@FieldMap Map<String, String> params);

    @POST(API_FETCH_TOPICS)
    @FormUrlEncoded
    Call<TopicsResponse> fetchTopics(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SUB_TOPICS)
    @FormUrlEncoded
    Call<SubTopicsResponse> fetchSubTopics(@FieldMap Map<String, String> params);

    @POST(API_FETCH_CONTENT)
    @FormUrlEncoded
    Call<ResponseBody> fetchContent(@FieldMap Map<String, String> params);

    @POST(API_FETCH_DASHBOARD)
    @FormUrlEncoded
    Call<DashboardResponse> fetchDashboardData(@FieldMap Map<String, String> params);

    @POST(API_FETCH_HOME)
    @FormUrlEncoded
    Call<TestsTabResponse> fetchTestsTabData(@FieldMap Map<String, String> params);

    @POST(API_FETCH_ALL_PACKAGES)
    @FormUrlEncoded
    Call<PackagesResponse> fetchAllPackages(@FieldMap Map<String, String> params);

    @POST(API_FETCH_TESTS)
    @FormUrlEncoded
    Call<TestsResponse> fetchAllTests(@FieldMap Map<String, String> params);

    @POST(API_FETCH_TEST_INSTRUCTIONS)
    @FormUrlEncoded
    Call<ResponseBody> fetchTestInstructions(@FieldMap Map<String, String> params);

    @POST(API_FETCH_QUESTIONS)
    @FormUrlEncoded
    Call<QuestionListResponse> fetchQuestions(@FieldMap Map<String, String> params);

    @POST(API_SUBMIT_TEST)
    @FormUrlEncoded
    Call<GeneralResponse> submitTest(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SCOREBOARD)
    @FormUrlEncoded
    Call<ScorecardResponse> fetchScorecard(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SOLUTIONS)
    @FormUrlEncoded
    Call<SolutionResponse> fetchSolutions(@FieldMap Map<String, String> params);

    @POST(API_FETCH_COMPAREDATA)
    @FormUrlEncoded
    Call<CompareResponse> fetchCompareData(@FieldMap Map<String, String> params);

    @POST(API_FETCH_MY_RESULTS)
    @FormUrlEncoded
    Call<MyResultsResponse> fetchMyResults(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SUPER_GROUPS)
    @FormUrlEncoded
    Call<SuperGroupResponse> fetchSuperGroups(@FieldMap Map<String, String> params);

    @POST(API_ADD_EXAM_PREFERENCES)
    @FormUrlEncoded
    Call<GeneralResponse> addExamPreferences(@FieldMap Map<String, String> params);

    @POST(API_CHECK_REFERRAL_CODE)
    @FormUrlEncoded
    Call<GeneralResponse> checkReferralCode(@FieldMap Map<String, String> params);

    @POST(API_COINS_HISTORY)
    @FormUrlEncoded
    Call<MyCoinsResponse> getCoinsHistory(@FieldMap Map<String, String> params);

    @POST(API_MY_EXAM_PREFERENCES)
    @FormUrlEncoded
    Call<MyExamPreferencesResponse> getMyExamPrefereces(@FieldMap Map<String, String> params);

    @POST(API_REMOVE_PREFERENCES)
    @FormUrlEncoded
    Call<GeneralResponse> removeExamPreferences(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SUBSCRIPTION_DETAILS)
    @FormUrlEncoded
    Call<ResponseBody> fetchSubscriptionDetails(@FieldMap Map<String, String> params);

    @POST(API_APPLY_COUPON)
    @FormUrlEncoded
    Call<ResponseBody> applyCoupon(@FieldMap Map<String, String> params);

    @POST(API_CREATE_ORDER)
    @FormUrlEncoded
    Call<ResponseBody> apiCreateOrder(@FieldMap Map<String, String> params);

    @POST(API_UPDATE_PAYMENT)
    @FormUrlEncoded
    Call<ResponseBody> apiUpdatePayment(@FieldMap Map<String, String> params);

    @POST(API_REDEEM_COINS)
    @FormUrlEncoded
    Call<ResponseBody> apiRedeemCoins(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SUBSCRIPTIONS_HISTORY)
    @FormUrlEncoded
    Call<SubscriptionResponse> apiFetchSubsctiptionHistory(@FieldMap Map<String, String> params);

    @POST(API_FETCH_SUBJECT_TOPICS)
    @FormUrlEncoded
    Call<SubjectTopicsResponse> fetchSubjectTopics(@FieldMap Map<String, String> params);

}
