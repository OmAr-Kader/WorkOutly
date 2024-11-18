import SwiftUI

extension UIUserInterfaceStyle {
    var isDarkMode: Bool {
        if self == .light {
            return false
        } else if self == .dark {
            return true
        }
        return false
    }
}

extension UIViewController {
    var appDelegate: AppDelegate {
    return UIApplication.shared.delegate as! AppDelegate
   }
}

extension UIColor {
    
    var toC: Color {
        return Color(self)
    }
    
    var rgba: (red: CGFloat, green: CGFloat, blue: CGFloat, alpha: CGFloat) {
        var red: CGFloat = 0
        var green: CGFloat = 0
        var blue: CGFloat = 0
        var alpha: CGFloat = 0
        getRed(&red, green: &green, blue: &blue, alpha: &alpha)
        
        return (red, green, blue, alpha)
    }
    
    var argb: Int64 {
        var alpha: CGFloat = 0
        var red: CGFloat = 0
        var green: CGFloat = 0
        var blue: CGFloat = 0
        
        
        getRed(&red, green: &green, blue: &blue, alpha: &alpha)
        
        
        let a = UInt32(alpha * 255) << 24
        let r = UInt32(red * 255) << 16
        let g = UInt32(green * 255) << 8
        let b = UInt32(blue * 255)
        return Int64(a | r | g | b)
    }
    
    func lighten(by factor: CGFloat) -> UIColor {
        var red: CGFloat = 0
        var green: CGFloat = 0
        var blue: CGFloat = 0
        var alpha: CGFloat = 0
        
        self.getRed(&red, green: &green, blue: &blue, alpha: &alpha)
        
        return UIColor(
            red: min(red + (1 - red) * factor, 1.0),
            green: min(green + (1 - green) * factor, 1.0),
            blue: min(blue + (1 - blue) * factor, 1.0),
            alpha: alpha
        )
    }
    
    func darken(by factor: CGFloat) -> UIColor {
        var red: CGFloat = 0
        var green: CGFloat = 0
        var blue: CGFloat = 0
        var alpha: CGFloat = 0
        
        self.getRed(&red, green: &green, blue: &blue, alpha: &alpha)
        
        return UIColor(
            red: red * (1 - factor),
            green: green * (1 - factor),
            blue: blue * (1 - factor),
            alpha: alpha
        )
    }
    
    convenience init(_ red: Double,_ green: Double,_ blue: Double) {
        self.init(red: red / 255, green: green / 255, blue: blue / 255, alpha: 1)
    }
    
}

extension Color {
    
    var toUic: UIColor {
        UIColor(self)
    }
    
    var margeWithPrimary: Color {
        return (toUic * 0.85 + Color(red: 109 / 255, green: 157 / 255, blue: 241 / 255).toUic * 0.15).toC
    }

    func margeWithPrimary(_ f: Double = 0.15) -> Color {
        return (toUic * (1.0 - f) + Color(red: 109 / 255, green: 157 / 255, blue: 241 / 255).toUic * f).toC
    }
    
    func margeColors(_ color: Color, _ f: Double) -> Color {
        return (toUic * (1.0 - f) + color.toUic * f).toC
    }
    
    static func random(randomOpacity: Bool = false) -> Color {
        Color(
            red: .random(in: 0...1),
            green: .random(in: 0...1),
            blue: .random(in: 0...1),
            opacity: randomOpacity ? .random(in: 0...1) : 1
        )
    }
    
    init(_ red: Double,_ green: Double,_ blue: Double) {
        self.init(red: red / 255, green: green / 255, blue: blue / 255)
    }
    
    init(argb: UInt32) {
        let alpha = Double((argb >> 24) & 0xFF) / 255.0
        let red = Double((argb >> 16) & 0xFF) / 255.0
        let green = Double((argb >> 8) & 0xFF) / 255.0
        let blue = Double(argb & 0xFF) / 255.0
        
        
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: alpha)
    }

    func getGradientFromBaseColor(second: Color) -> LinearGradient {
        // Create a linear gradient with this color and the second color
        return LinearGradient(
            gradient: Gradient(colors: [self, second]),
            startPoint: .topLeading,
            endPoint: .bottomTrailing
        )
    }
    
    func darken(by factor: CGFloat) -> Color {
        return toUic.darken(by: factor).toC
    }
    
    func lighten(by factor: CGFloat) -> Color {
        return toUic.lighten(by: factor).toC
    }
}


func addColor(_ color1: UIColor, with color2: UIColor) -> UIColor {
    var (r1, g1, b1, a1) = (CGFloat(0), CGFloat(0), CGFloat(0), CGFloat(0))
    var (r2, g2, b2, a2) = (CGFloat(0), CGFloat(0), CGFloat(0), CGFloat(0))
    color1.getRed(&r1, green: &g1, blue: &b1, alpha: &a1)
    color2.getRed(&r2, green: &g2, blue: &b2, alpha: &a2)

    // add the components, but don't let them go above 1.0
    return UIColor(red: min(r1 + r2, 1), green: min(g1 + g2, 1), blue: min(b1 + b2, 1), alpha: (a1 + a2) / 2)
}

func multiplyColor(_ color: UIColor, by multiplier: CGFloat) -> UIColor {
    var (r, g, b, a) = (CGFloat(0), CGFloat(0), CGFloat(0), CGFloat(0))
    color.getRed(&r, green: &g, blue: &b, alpha: &a)
    return UIColor(red: r * multiplier, green: g * multiplier, blue: b * multiplier, alpha: a)
}

func +(color1: UIColor, color2: UIColor) -> UIColor {
    return addColor(color1, with: color2)
}

func *(color: UIColor, multiplier: Double) -> UIColor {
    return multiplyColor(color, by: CGFloat(multiplier))
}

func rateColor(rate: Double) -> Color {
    return Color(UIColor(.yellow) * 0.5 + .darkGray * 0.5)
}


var shadowColor: Color {
    //Color(red: 0, green: 0, blue: 0, opacity: 50)
    return UIColor(_colorLiteralRed: 0, green: 0, blue: 0, alpha: 0.5).toC
}

var Green: Color {
    return Color(red: 1 / 255, green: 189 / 255, blue: 1 / 255)
}

var Blue: Color {
    return Color(red: 13 / 255, green: 23 / 255, blue: 213 / 255)
}

var Yellow: Color {
    return Color(red: 224 / 255, green: 224 / 255, blue: 12 / 255)
}

var BackSec: Color {
    return Color(red: 61 / 255, green: 61 / 255, blue: 61 / 255)
}

var BackSecDark: Color {
    return Color(red: 201 / 255, green: 201 / 255, blue: 201 / 255)
}

var colorBarIOS: Color {
    return Color(red: 9 / 255, green: 131 / 255, blue: 1)
}
