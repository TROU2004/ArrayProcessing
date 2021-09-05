package it.zerono.mods.zerocore.lib.client;

import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.util.CodeHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class VersionChecker {
      private static List s_pendingNotifications;

      public static void scheduleCheck(String url) {
            ModContainer mc = Loader.instance().activeModContainer();
            if (null != mc && null != url && !url.isEmpty()) {
                  StringBuilder sb = new StringBuilder(url);
                  sb.append("?mod=");
                  sb.append(mc.getModId());
                  sb.append("&mc=");
                  sb.append("1.12.2");

                  URL checkURL;
                  try {
                        checkURL = new URL(sb.toString());
                  } catch (MalformedURLException var5) {
                        var5.printStackTrace();
                        return;
                  }

                  (new VersionChecker.VersionCheckerThread(new VersionChecker.ModVersionData(mc, checkURL))).start();
            }
      }

      @SubscribeEvent
      public static void onPlayerLoggedIn(PlayerLoggedInEvent evt) {
            List notifications = getPendingNotifications();
            if (null != notifications) {
                  Iterator var2 = notifications.iterator();

                  while(true) {
                        VersionChecker.ModVersionData versionData;
                        do {
                              do {
                                    if (!var2.hasNext()) {
                                          return;
                                    }

                                    versionData = (VersionChecker.ModVersionData)var2.next();
                              } while(null == versionData);
                        } while(!versionData.isNewVersionAvailable());

                        String updateMessage = versionData.getUpdateMessage();
                        TextComponentTranslation msg;
                        if (null != updateMessage && !updateMessage.isEmpty()) {
                              msg = new TextComponentTranslation("zerocore:vercheck.update2", new Object[]{versionData.getLastVersion(), versionData.getName(), updateMessage});
                        } else {
                              msg = new TextComponentTranslation("zerocore:vercheck.update1", new Object[]{versionData.getLastVersion(), versionData.getName()});
                        }

                        CodeHelper.sendChatMessage(evt.player, msg);
                  }
            }
      }

      private static synchronized void addNotification(VersionChecker.ModVersionData data) {
            if (null == s_pendingNotifications) {
                  s_pendingNotifications = new ArrayList();
            }

            s_pendingNotifications.add(data);
      }

      private static synchronized List getPendingNotifications() {
            List notifications = s_pendingNotifications;
            s_pendingNotifications = null;
            return notifications;
      }

      private static class VersionCheckerThread extends Thread {
            private final VersionChecker.ModVersionData _modData;

            public VersionCheckerThread(VersionChecker.ModVersionData modVersionData) {
                  this.setName("Zero CORE version checker thread");
                  this._modData = modVersionData;
            }

            public void run() {
                  HttpURLConnection cn = this.openConnection();
                  String reply = null;

                  try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(cn.getInputStream()));
                        Throwable var4 = null;

                        try {
                              reply = reader.readLine();
                        } catch (Throwable var14) {
                              var4 = var14;
                              throw var14;
                        } finally {
                              if (reader != null) {
                                    if (var4 != null) {
                                          try {
                                                reader.close();
                                          } catch (Throwable var13) {
                                                var4.addSuppressed(var13);
                                          }
                                    } else {
                                          reader.close();
                                    }
                              }

                        }
                  } catch (Exception var16) {
                        var16.printStackTrace();
                  }

                  if (null != reply && !reply.isEmpty()) {
                        String[] tokens = reply.split(";");
                        if (0 != tokens.length) {
                              if (0 == tokens[0].compareToIgnoreCase("OK") && tokens.length > 1) {
                                    String lastVersion = tokens[1];
                                    String updateMessage = tokens.length > 2 ? tokens[2] : "";
                                    this._modData.update(lastVersion, updateMessage);
                                    VersionChecker.addNotification(this._modData);
                              } else {
                                    ZeroCore.getLogger().info("Update check for mod %s failed : %s", this._modData.getName(), reply);
                              }

                        }
                  }
            }

            private HttpURLConnection openConnection() {
                  try {
                        HttpURLConnection cn = (HttpURLConnection)this._modData.getCheckURL().openConnection();
                        cn.setConnectTimeout(3000);
                        cn.setReadTimeout(3000);
                        cn.setRequestMethod("GET");
                        return cn;
                  } catch (Exception var2) {
                        var2.printStackTrace();
                        return null;
                  }
            }
      }

      private static class ModVersionData {
            private final ModContainer _container;
            private final URL _checkURL;
            private String _lastVersion;
            private String _updateMessage;

            public ModVersionData(ModContainer container, URL url) {
                  this._container = container;
                  this._checkURL = url;
                  this._lastVersion = this._updateMessage = null;
            }

            public String getName() {
                  return this._container.getName();
            }

            public URL getCheckURL() {
                  return this._checkURL;
            }

            public String getCurrentVersion() {
                  return this._container.getVersion();
            }

            public String getLastVersion() {
                  return this._lastVersion;
            }

            public String getUpdateMessage() {
                  return this._updateMessage;
            }

            public void update(String lastVersion, String updateMessage) {
                  this._lastVersion = lastVersion;
                  this._updateMessage = updateMessage;
            }

            public boolean isNewVersionAvailable() {
                  ComparableVersion current = new ComparableVersion(this.getCurrentVersion());
                  ComparableVersion last = new ComparableVersion(this.getLastVersion());
                  return last.compareTo(current) > 0;
            }
      }
}
