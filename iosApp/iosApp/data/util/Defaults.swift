//
//  Defaults.swift
//  iosApp
//
//  Created by OmAr on 14/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

//@BackgroundActor
extension UserPref : @unchecked @retroactive Sendable {
    
    func copy(authId: String? = nil, id: String? = nil, email: String? = nil, phone: String? = nil, name: String? = nil, profilePicture: String? = nil) -> UserPref {
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

extension Message : @unchecked @retroactive Sendable {
    
    convenience init(id: String, userId: String, senderName: String, message: String, fileUrl: String, type: Int32, session: String, date: String) {
        self.init(id: id, userId: userId, senderName: senderName, message: message, fileUrl: fileUrl, type: type, session: session, date: date, isFromCurrentUser: false)
    }
    
    func copy(id: String? = nil, userId: String? = nil, senderName: String? = nil, message: String? = nil, fileUrl: String? = nil, type: Int32? = nil, session: String? = nil, date: String? = nil, isFromCurrentUser: Bool? = nil) -> Message {
        return Message(id: id ?? self.id, userId: userId ?? self.userId, senderName: senderName ?? self.senderName, message: message ?? self.message, fileUrl: fileUrl ?? self.fileUrl, type: type ?? self.type, session: session ?? self.session, date: date ?? self.date, isFromCurrentUser: isFromCurrentUser ?? self.isFromCurrentUser)
    }
}

extension Exercise : @unchecked @retroactive Sendable {
 
    func copy(id: String? = nil, date: String? = nil, title: String? = nil, videoUrl: String? = nil, length: Int64? = nil, cato: String? = nil, views: Int64? = nil, liker: [String]? = nil, desc: String? = nil, isLiked: Bool? = nil, likes: Int32? = nil) -> Exercise {
        return Exercise(id: id ?? self.id, date: date ?? self.date, title: title ?? self.title, videoUrl: videoUrl ?? self.videoUrl, length: length ?? self.length, cato: cato ?? self.cato, views: views ?? self.views, liker: liker ?? self.liker, desc: desc ?? self.desc, isLiked: isLiked ?? self.isLiked, likes: likes ?? self.likes)
    }
}

extension KotlinInt : @unchecked @retroactive Sendable {
    
}

extension KotlinByteArray : @unchecked @retroactive Sendable {
    
}

extension UIImage : @unchecked @retroactive Sendable {
    
}

@BackgroundActor
func convertToKotlinByteArray(from byteArray: [UInt8]) -> KotlinByteArray {
    let kotlinByteArray = KotlinByteArray(size: Int32(byteArray.count))
    for (index, byte) in byteArray.enumerated() {
        kotlinByteArray.set(index: Int32(index), value: Int8(bitPattern: byte))
    }
    return kotlinByteArray
}
