//
//  Defaults.swift
//  iosApp
//
//  Created by OmAr on 14/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import shared

//@BackgroundActor
extension UserPref : @unchecked @retroactive Sendable {
    
    func copy(authId: String? = nil, id: Int64? = nil, email: String? = nil, phone: String? = nil, name: String? = nil, profilePicture: String? = nil) -> UserPref {
        return UserPref(authId: authId ?? self.authId, id: id ?? self.id, email: email ?? self.email, phone: phone ?? self.phone, name: name ?? self.name, profilePicture: profilePicture ?? self.profilePicture)
    }
}

extension PreferenceData : @unchecked @retroactive Sendable {
    
    func copy(id: String? = nil, keyString: String? = nil, value: String? = nil) -> PreferenceData {
        return PreferenceData(id: id ?? self.id, keyString: keyString ?? self.keyString, value: value ?? self.value)
    }
}

extension FitnessMetric : @unchecked @retroactive Sendable, @retroactive Identifiable {

    convenience init(id: Int32, title: String, iconColor: Int64, value: Int64, valueUnit: String) {
        self.init(id: id, title: title, iconColor: iconColor, value: value, valueUnit: valueUnit, valueStr: value.toString + valueUnit)
    }

}

extension FitnessHistoryMetric : @unchecked @retroactive Sendable {
    
    convenience init(milliSec: Int64, data: String, value: Int64, valueUnit: String) {
        self.init(milliSec: milliSec, data: data, value: value, valueUnit: valueUnit, isMoreThanPreviousSession: true)
    }
}

extension Project : @unchecked @retroactive Sendable {
    
}

extension KotlinInt : @unchecked @retroactive Sendable {
    
}

