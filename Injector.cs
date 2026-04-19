using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;

namespace CloverInjector
{
    class Program
    {
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
        const uint PAGE_EXECUTE_READWRITE = 0x40;

        static void Main(string[] args)
        {
            string dllName = "CloverDLL.dll";
            string dllPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, dllName);

            if (!File.Exists(dllPath))
            {
                Console.WriteLine("Error: " + dllName + " not found.");
                Console.ReadKey();
                return;
            }

            Process targetProcess = Process.GetProcessesByName("javaw").FirstOrDefault();
            if (targetProcess == null)
            {
                Console.WriteLine("No Minecraft Found");
                Console.ReadKey();
                return;
            }

            if (Inject(targetProcess.Id, dllPath))
            {
                Console.WriteLine("Injection Successful");
            }
            else
            {
                Console.WriteLine("Injection Failed");
            }

            Console.ReadKey();
        }

        static bool Inject(int processId, string dllPath)
        {
            IntPtr hProcess = OpenProcess(PROCESS_ALL_ACCESS, false, processId);
            if (hProcess == IntPtr.Zero) return false;

            IntPtr loadLibraryAddr = GetProcAddress(GetModuleHandle("kernel32.dll"), "LoadLibraryA");
            if (loadLibraryAddr == IntPtr.Zero) return false;

            byte[] pathBytes = Encoding.ASCII.GetBytes(dllPath + "\0");
            uint pathSize = (uint)pathBytes.Length;

            IntPtr allocMem = VirtualAllocEx(hProcess, IntPtr.Zero, pathSize, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
            if (allocMem == IntPtr.Zero) return false;

            WriteProcessMemory(hProcess, allocMem, pathBytes, pathSize, out _);

            IntPtr hThread = CreateRemoteThread(hProcess, IntPtr.Zero, 0, loadLibraryAddr, allocMem, 0, IntPtr.Zero);

            return hThread != IntPtr.Zero;
        }
    }
}