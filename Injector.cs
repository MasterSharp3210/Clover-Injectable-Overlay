using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;
using System.Text;

namespace CloverInjector
{
    class Program
    {
        // Import WINAPI functions for process manipulation
        [DllImport("kernel32.dll", SetLastError = true)]
        static extern IntPtr OpenProcess(uint dwDesiredAccess, bool bInheritHandle, int dwProcessId);

        [DllImport("kernel32.dll", SetLastError = true)]
        static extern IntPtr GetModuleHandle(string lpModuleName);

        [DllImport("kernel32.dll", SetLastError = true)]
        static extern IntPtr GetProcAddress(IntPtr hModule, string lpProcName);

        [DllImport("kernel32.dll", SetLastError = true)]
        static extern IntPtr VirtualAllocEx(IntPtr hProcess, IntPtr lpAddress, uint dwSize, uint flAllocationType, uint flProtect);

        [DllImport("kernel32.dll", SetLastError = true)]
        static extern bool WriteProcessMemory(IntPtr hProcess, IntPtr lpBaseAddress, byte[] lpBuffer, uint nSize, out IntPtr lpNumberOfBytesWritten);

        [DllImport("kernel32.dll", SetLastError = true)]
        static extern IntPtr CreateRemoteThread(IntPtr hProcess, IntPtr lpThreadAttributes, uint dwStackSize, IntPtr lpStartAddress, IntPtr lpParameter, uint dwCreationFlags, IntPtr lpThreadId);


        const uint PROCESS_ALL_ACCESS = 0x001F0FFF;
        const uint MEM_COMMIT = 0x1000;
        const uint MEM_RESERVE = 0x2000;
        const uint PAGE_READWRITE = 0x04;

        static void Main(string[] args)
        {
            string exePath = AppDomain.CurrentDomain.BaseDirectory;
            string dllName = "CloverDLL.dll";
            string dllPath = Path.Combine(exePath, dllName);

            if (!File.Exists(dllPath))
            {
                Console.WriteLine($"Error: {dllName} not found in the program folder...");
                Console.ReadKey();
                return;
            }

            Process[] processes = Process.GetProcessesByName("javaw");
            if (processes.Length == 0)
            {
                Console.WriteLine("No Minecraft Found");
                Console.ReadKey();
                return;
            }

            Process targetProcess = processes[0];
            Console.WriteLine($"Minecraft Process Found | PID: {targetProcess.Id}");


            if (Inject(targetProcess.Id, dllPath))
            {
                Console.WriteLine("Injection Successful"); Console.ReadKey();
            }
            else
            {
                Console.WriteLine("Injection Failed");
            }

            Console.WriteLine("Press any key to exit...");
            Console.ReadKey();
        }

        static bool Inject(int processId, string dllPath)
        {
            IntPtr hProcess = OpenProcess(PROCESS_ALL_ACCESS, false, processId);
            if (hProcess == IntPtr.Zero) return false;

            IntPtr loadLibraryAddr = GetProcAddress(GetModuleHandle("kernel32.dll"), "LoadLibraryA");
            if (loadLibraryAddr == IntPtr.Zero) return false;


            uint pathSize = (uint)((dllPath.Length + 1) * Marshal.SizeOf(typeof(char)));
            IntPtr allocMem = VirtualAllocEx(hProcess, IntPtr.Zero, pathSize, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);

            WriteProcessMemory(hProcess, allocMem, Encoding.Default.GetBytes(dllPath), pathSize, out _);

            IntPtr hThread = CreateRemoteThread(hProcess, IntPtr.Zero, 0, loadLibraryAddr, allocMem, 0, IntPtr.Zero);

            return hThread != IntPtr.Zero;
        }
    }
}