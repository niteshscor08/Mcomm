package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.CallsRepository
import javax.inject.Inject

class GetCallsUseCase @Inject constructor(
    private val callsRepository: CallsRepository
) {
}