import SwiftUI
import ComposeApp

struct ComposeContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController() // KMP entry point
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
