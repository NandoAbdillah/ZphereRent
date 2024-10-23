<?php

namespace App\Http\Controllers\Auth;

use App\Models\User;
use Illuminate\Support\Str;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Password;
use App\Http\Requests\Auth\SignupRequest;
use Illuminate\Auth\Events\PasswordReset;
use Illuminate\Support\Facades\Validator;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        $messages = [
            'name.required' => 'Nama wajib diisi.',
            'name.string' => 'Nama harus berupa teks.',
            'name.max' => 'Nama tidak boleh lebih dari 255 karakter.',
            'email.required' => 'Email wajib diisi.',
            'email.string' => 'Email harus berupa teks.',
            'email.email' => 'Format email tidak valid.',
            'email.max' => 'Email tidak boleh lebih dari 255 karakter.',
            'email.unique' => 'Email sudah terdaftar.',
            'password.required' => 'Password wajib diisi.',
            'password.string' => 'Password harus berupa teks.',
            'password.min' => 'Password minimal harus 8 karakter.',
            'password.confirmed' => 'Konfirmasi password tidak sesuai.',
            'password.regex' => 'Password minimal satu huruf dan satu angka.',
        ];
    
        $validator = Validator::make($request->all(), [
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => [
                'required',
                'string',
                'min:8',
                'confirmed',
                'regex:/^(?=.*[a-zA-Z])(?=.*\d).+$/'
            ],
        ], $messages);
    
        if ($validator->fails()) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => $validator->errors()->first(),
            ], 400);
        }
    
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
        ]);
    
        // Generate token
        $token = $user->createToken('auth_token')->plainTextToken;
    
        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Pendaftaran pengguna berhasil.',
            'user' => $user,
            'token' => $token,
        ], 201);
    }

    // LOGIN
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'password' => 'required|string',
        ]);


        if ($validator->fails()) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => $validator->errors()->first(),
            ], 400); // Menggunakan status code 400 untuk Bad Request
        }

        $user = User::where('email', $request->email)->first();

        if (!$user || !Hash::check($request->password, $user->password)) {
            return response()->json(['message' => 'Password atau email salah'], 401);
        }

        // Generate token
        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Login successful',
            'user' => $user,
            'token' => $token,
        ], 200);
    }


    // FORGOT PASSWORD
    public function forgotPassword(Request $request)
    {
        $request->validate(['email' => 'required|email']);

        // Cek apakah user dengan email tersebut ada
        $user = User::where('email', $request->email)->first();
        if (!$user) {
            return response()->json([
                'error' => true,
                'message' => 'Email not found.',
            ], 404);
        }

        // Generate token 6 digit
        $token = random_int(100000, 999999);

        // Simpan token di database atau gunakan tabel password_resets
        DB::table('password_reset_tokens')->updateOrInsert(
            ['email' => $request->email],
            [
                'email' => $request->email,
                'token' => Hash::make($token),
                'created_at' => now(),
            ]
        );

        // Kirim email dengan token
        Mail::send('emails.forgot-password', ['token' => $token], function ($message) use ($request) {
            $message->to($request->email);
            $message->subject('Reset Password Notification');
        });

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Reset password token has been sent to your email.',
            'data' => [
                'email' => $request->email,
                'token_sent' => true
            ]
        ], 200);
    }

    // VERIFY TOKEN 
    public function verifyToken(Request $request)
    {
        $request->validate([
            'token' => 'required',
            'email' => 'required|email',
        ]);

        // Cari reset token di database
        $reset = DB::table('password_reset_tokens')
            ->where('email', $request->email)
            ->first();

        if (!$reset || !Hash::check($request->token, $reset->token)) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => 'Invalid token or email.',
                'data' => null
            ], 400);
        }

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Token is valid. You may now reset your password.',
            'data' => [
                'email' => $request->email,
                'token_verified' => true
            ]
        ], 200);
    }


    // RESET PASSWORD
    public function resetPassword(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'password' => 'required|string|min:8|confirmed',
        ]);

        // Cari reset token di database
        $reset = DB::table('password_reset_tokens')
            ->where('email', $request->email)
            ->first();

        if (!$reset) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => 'Token not found. Please request a new reset link.',
                'data' => null
            ], 400);
        }

        // Update password user
        $user = User::where('email', $request->email)->first();
        if (!$user) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => 'User not found.',
                'data' => null
            ], 404);
        }

        $user->forceFill([
            'password' => Hash::make($request->password),
        ])->setRememberToken(Str::random(60));

        $user->save();

        // Hapus token reset setelah digunakan
        DB::table('password_reset_tokens')->where('email', $request->email)->delete();

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Password has been reset successfully.',
            'data' => [
                'email' => $request->email,
                'password_reset' => true
            ]
        ], 200);
    }





    // LOGOUT
    public function logout(Request $request)
    {
        $request->user()->tokens()->delete();

        return response()->json([
            'message' => 'Logged out successfully'
        ], 200);
    }
}
