package com.panevrn.employmentseekershub

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient() {
    private lateinit var authService: AuthService
    private lateinit var vacancyService: VacancyService

    fun getAuthService(): AuthService {

        if (!::authService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            authService = retrofit.create(AuthService::class.java)
        }

        return authService
    }

    fun getVacancyService(): VacancyService {
        if (!::vacancyService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            vacancyService = retrofit.create(VacancyService::class.java)
        }

        return vacancyService
    }

}



//  testList = mutableListOf(
//            VacancyDto(
//                id = "1",
//                vacancyTitle = "Product Designer",
//                companyTitle = "MetaMask",
//                countCandidates = 25,
//                isLiked = false,
//                tags = listOf("Entry Level", "Full-Time"),
//                description = "Innovate and design new user experiences.",
//                salary = "$250/hr",
//                postedTime = "12 days ago"
//            ),
//            VacancyDto(
//                id = "2",
//                vacancyTitle = "Frontend Developer",
//                companyTitle = "Decentraland",
//                countCandidates = 40,
//                isLiked = true,
//                tags = listOf("Mid-Level", "Full-Time", "Remote"),
//                description = "Develop cutting-edge web applications.",
//                salary = "$300/hr",
//                postedTime = "3 days ago"
//            ),
//            VacancyDto(
//                id = "3",
//                vacancyTitle = "Blockchain Engineer",
//                companyTitle = "Chainlink",
//                countCandidates = 10,
//                isLiked = true,
//                tags = listOf("Senior Level", "Part-Time"),
//                description = "Build decentralized networks.",
//                salary = "$350/hr",
//                postedTime = "5 days ago"
//            ),
//            VacancyDto(
//                id = "4",
//                vacancyTitle = "UX Researcher",
//                companyTitle = "Uniswap",
//                countCandidates = 15,
//                isLiked = false,
//                tags = listOf("Entry Level", "Contract"),
//                description = "Conduct user research and tests.",
//                salary = "$150/hr",
//                postedTime = "2 weeks ago"
//            ),
//            VacancyDto(
//                id = "5",
//                vacancyTitle = "Product Designer",
//                companyTitle = "MetaMask",
//                countCandidates = 25,
//                isLiked = false,
//                tags = listOf("Entry Level", "Full-Time"),
//                description = "Innovate and design new user experiences.",
//                salary = "$250/hr",
//                postedTime = "12 days ago"
//            ),
//            VacancyDto(
//                id = "6",
//                vacancyTitle = "Frontend Developer",
//                companyTitle = "Decentraland",
//                countCandidates = 40,
//                isLiked = true,
//                tags = listOf("Mid-Level", "Full-Time", "Remote"),
//                description = "Develop cutting-edge web applications.",
//                salary = "$300/hr",
//                postedTime = "3 days ago"
//            ),
//            VacancyDto(
//                id = "7",
//                vacancyTitle = "Blockchain Engineer",
//                companyTitle = "Chainlink",
//                countCandidates = 10,
//                isLiked = false,
//                tags = listOf("Senior Level", "Part-Time"),
//                description = "Build decentralized networks.",
//                salary = "$350/hr",
//                postedTime = "5 days ago"
//            ),
//            VacancyDto(
//                id = "8",
//                vacancyTitle = "UX Researcher",
//                companyTitle = "Uniswap",
//                countCandidates = 15,
//                isLiked = true,
//                tags = listOf("Entry Level", "Contract"),
//                description = "Conduct user research and tests.",
//                salary = "$150/hr",
//                postedTime = "2 weeks ago"
//            )
//        )