package com.we2dx.hodop.utils

class ApplicationConstants {
        companion object {
            const val SPLASHSCREEN_TIME = 2000L
            val PASSCODE_TIME = 300L
            val LOGIN_STATE = "com.we2dx.drCloud.LoginStatus"
            val HAS_LOGIN = "isLoggedIn"
            const val HAS_TAKEN_A_PEEK = "hasTakenPeek"
            const val HAS_ACCEPT_TC = "HAS ACCEPTED TC"
            const val TRAFFIC_INFO = "TRAFFIC INFO"
            const val HAS_ACCEPT_TRAFFIC_RULE = "HAS ACCEPTED TRAFFIC RULES"
            const val HAS_RUN = "hasRun"
            val MY_DOCTOR = "My Doctor"
            val UPLOAD_SUCCESSFUL = "upload success"
            val UPLOAD_FAIL = "upload fail"
            val SUB_PLAN_TYPE = "sub_plan_type"
            val AMOUNT_TO_PAY = "amount_to_pay"
            val SERVICE_PAYMENT = "service to pay for"
            val SERVICE_EXTRA_INFO = "service extra info"

            const val ACTIVE = "Active"
            const val CANCELLED = "Cancelled"
            const val PENDING = "Pending"

            val SPLASHSCREEN = "splashscreen"

            val WALLETS = "Wallets"
            const val CARDS = "Card"
            const val TRANSACTION_DETAILS = "Transaction Details"
            const val TRANSACTION_TOP_UP = "Top up"
            const val TRANSACTION_SUBSCRIPTION = "Subscription"
            const val TRANSACTION_PAY_BILLS = "Pay Bill"
            const val ACCOUNT_BALANCE = "AccountBalance"

            val VERIFY_OTP = "verify otp"
            val CUSTOMER_CARE   = "customerCare"
            val MY_CARE_LINE   = "myCareLine"
            val CONVERSATION = "Conversation"
            val IS_USER_TYPING = "isTyping"
            val GOT_USER_DATA = "got user data"
            val NO_USER_DATA = "no user data"
            val USER_DATA_ERROR = "user data error"
            val DOCTOR_CHAT = "Message Doctor"
            val CLOUDER_CHAT = "Message Cloud"
            val SUPPORT_CHAT = "Message Support"
            val TASK_SUCCESS = "task successful"
            val STARTER_PLAN = "STARTER"
            val BASIC_PLAN = "BASIC"
            val PREMIUM_PLAN = "PREMIUM"
            val STARTER_PRICE = "STARTER PRICE"
            val BASIC_PRICE = "BASIC PRICE"
            val PREMIUM_PRICE = "PREMIUM PRICE"
            val STARTER_DURATION = "STARTER DURATION"
            val BASIC_DURATION = "BASIC DURATION"
            val PREMIUM_DURATION = "PREMIUM DURATION"
            val SUBSCRIBE = "SUBSCRIBE"
            val PRIVACY = "PRIVACY"
            val SUBSCRIBE_INFO = "With DrCloud, you can communicate with your personal" +
                    " licensed physician anytime, anywhere"
            val PRIVACY_INFO = "Customized your chat history to enable access to content to be display "

            val PASSCODE = "passcode"
            val PASSCODE_STATE = "passcode state"

            val YES = "Yes"
            val NO = "No"
            val NA = "NA"

            val DIABETES_PATIENT = "diabetes_patient"
            val SMOKER_PATIENT = "smoker_patient"
            val HIGH_BP_PATIENT = "high_bp_patient"
            val ALCOHOLIC_PATIENT = "alcoholic_patient"

            val CHAT= "chat"
            val GOT_CARELINE ="got careline"
            val MESSAGES = "Messages"
            val CHAT_THREAD = "ChatThread"
            val USER_FULL_NAME = "fullname"
            val USER_CREATED_WITH_EMAIL_SUCCESS = "user create with email successfully"

            val USER_CREATED_WITH_EMAIL_FAILED = "user create with email fail"
            val SUCCESS_SAVE_INFO = "save user info suuccessfully"

            val VerificationCompleted = "Verified"
            val  VerificationFailed = "Failed"
            val  VerificationCodeIncorrect = "Code wrong"
            val VerificationCodeSent = "CodeSent"
            val CodeTimeout = "CodeExpired"

            val PASSWORD = "password"
            val NOT_TYPING = "not typing"
            val TYPING = "typing"
            val EMAIL = "email"
            val MESSAGE_COUNT_SUCCESS =  "success"
            val MESSAGE_COUNT_FAILED =  "success"

            val SERVICE_TYPE = "service_type"
            val HEIGHT = "height"
            val WEIGHT = "weight"
            val OTHER_INFO = "other_info"
            val HEALTH_BACKGROUND = "health_background"
            val ALLERGIES = "allergies"
            val MEDICATIONS = "medications"

            val DESTINATION_TYPE = "destination"
            val CURRENT_LOCATION_TYPE = "current_location"




            val FEMALE = "female"
            val MALE = "male"
            val INVALID_COUNTRY_CODE = "Invalid country code"

            val NAME = "name"
            val USER_UID = "user uid"
            val PHONE_NO = "phoneNo"
            val GENDER = "gender"
            val MYSELF = "Myself"
            val isUserMapped = "mapToDoc"

            val permissions = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val FONT_BOLD = "Montserrat-Bold.ttf"
            val FONT_REGULAR = "Montserrat-Regular.ttf"
            val FONT_BOLD_SIZE = 18F
            val FONT_REGULAR_SIZE = 18F
            val NAV_FONT_SIZE = 48F
            val FONT_LARGE = 22F
            val FONT_MEDIUM = 20F
            val COUNTRY_CODE = "+234"
            val ACTION_OK = "OK"
            val FALSE = false
            val TAG = "drCloud"
            val MY_PREFS_NAME = "drCloud"
            val SUCCESS_RESP = "Success"

            val DR_CLOUD = "Dr Cloud"

            val FAIL_RESP = "Failed"
            val CREATE_AUTH_FAIL = "Could not create Authentication"
            val SIGN_IN_FAIL = "Could not sign in"
            val EMPTY = "NA"
            val USERS = "Users"
            val NICKNAME = "nickname"
            val MSISDN = "Mobile Number"
            val OTP_TIME = 90L
            val CODE_SENT = "Code Sent"
            val CODE_TIME_OUT = "Code Auto-retrieval time-out"
            val TASK_CANCELLED = "On Cancelled"
            val AUTH_SUCCESS = "Successful Authentication"
            val CREATE_AUTH_SUCCESS = "Successfully created Authentication"
            val SIGN_IN_SUCCESS = "Successfully sign in"
            val NO_GENDER = "No Gender Selected"
            val EMPTY_FIELDS = "Empty Fields"
            val FUTURE_DATE = "Future Date"
            val EMAIL_REGEX = "[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
            val EMAIL_NOMATCH = "Not a valid Email"
            val MSISDN_REGEX = "^[0-9]{10}\$"
            val MSISDN_NOMATCH = "Not a valid 10 digit number"
            val REGISTRATION = "Registration"
            val LOGIN = "Login"
            val WELCOME_TYPE = "WelcomeType"
            val SPLASH_TIME = 3000L
            val DOC_CATEGORIES = "DoctorCategories"
            val POSITIVE_BUTTON = "YES"
            val NEGATIVE_BUTTON = "NO"
            val DATABASE_MSISDN = "msisdn"
            val DATABASE_EMAIL = "email"
            val UPDATE_PROF_FAIL = "Update profile failed"
            val UPDATE_PROF_SUCCESS = "Update profile success"
            val UPDATE_EMAIL_SUCCESS = "Update email success"
            val UPDATE_EMAIL_FAIL = "Update email failed"
            val NO_RATING = "-"
            val NEW_PROFILE = "New User"
            val SNACKBAR_ACTION = "Snack bar button clicked"
            val DOCTOR_CATEGORY = "Doc category"
            val NUM_EDITED = "Number edited"
            val NUM_EDITED_GO = "Change"
            val NUM_EDITED_NOGO = "Don't change"
            val GOTO_LOGIN = "Logout"
            val DOCTORS = "Doctors"
            val CARE_LINE = "careline"
            val CLOUDER = "clouder"
            val DOCTOR_NAME = "DoctorBio name"
            val DOC_FROM_TIME = "Available from time"
            val DOC_TO_TIME = "Available to time"
            val REQUEST_CODE = 101
            val USER_LEFT = "Remote user left"
            val USER_MUTED = "Remote user muted"
            val SPEAKER_ENABLED = "Speaker enabled"
            val USER_ONLINE = "1"
            val USER_OFFLINE = "0"
        }
}